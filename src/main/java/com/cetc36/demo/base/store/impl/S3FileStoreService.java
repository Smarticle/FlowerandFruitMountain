package com.cetc36.demo.base.store.impl;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.cetc36.demo.base.store.FileStoreService;
import com.cetc36.demo.common.enums.ErrorCodeEnum;
import com.cetc36.demo.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class S3FileStoreService implements FileStoreService {

    @Value("${aws.accesskey}")
    private String accessKey;

    @Value("${aws.secretkey}")
    private String secretKey;

    @Value("${aws.endpoint}")
    private String endpoint;

    private AmazonS3 s3client;


    @PostConstruct
    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        ClientConfiguration configuration = new ClientConfiguration();
        configuration
                .withConnectionTimeout(2 * 1000)
                .withSocketTimeout(5 * 1000)
                .withRequestTimeout(60 * 1000)
                .withClientExecutionTimeout(120 * 1000)
                .withProtocol(Protocol.HTTP);

        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, null);

        s3client = AmazonS3ClientBuilder
                .standard()
                .withClientConfiguration(configuration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endpointConfiguration)
                .withPathStyleAccessEnabled(true)
                .build();
    }

    @PreDestroy
    public synchronized void destroy() {
        if (s3client != null) {
            s3client.shutdown();
            s3client = null;
        }
    }

    @Override
    public String upload(InputStream inputStream, String targetContainer, String key) {
        PutObjectResult result = s3client.putObject(
                targetContainer,
                key,
                inputStream, new ObjectMetadata());
        log.info("uploaded file to s3,{}/{},md5:{}", targetContainer, key, result.getContentMd5());
        return key;
    }

    @Override
    public String download(String targetContainer, String key, String savePath) {
        S3Object s3object = s3client.getObject(targetContainer, key);
        String savedFile = getSavedLocal(key, savePath);
        try (S3ObjectInputStream inputStream = s3object.getObjectContent()) {
            FileUtils.copyInputStreamToFile(inputStream, new File(savedFile));
            return savedFile;
        } catch (IOException e) {
            log.error("download from s3 error.{}/{}", targetContainer, key);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件下载路径
     */
    public String generateDownloadUrl(String targetContainer, String key, int expiredTime) {
        try {
            if (StringUtils.isBlank(key) || StringUtils.isBlank(targetContainer)) {
                return null;
            }
            return s3client.generatePresignedUrl(targetContainer, key, Date.from(LocalDateTime.now().plusMinutes(expiredTime).atZone(ZoneId.systemDefault()).toInstant())).toString();
        } catch (Exception e) {
            log.error("获取下载文件路径异常：{}", key, e);
            throw new BusinessException(ErrorCodeEnum.FILE_ERROR);
        }
    }

    private String getSavedLocal(String key, String savePath) {
        if (savePath.endsWith("/")) {
            return savePath + key;
        }
        return savePath + "/" + key;
    }
}
