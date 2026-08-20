//package com.cetc36.demo.service.impl;
//
//import com.minnovation.common.entities.FileEncrypt;
//import com.minnovation.common.enums.ErrorCodeEnum;
//import com.minnovation.common.enums.ErrorMessageEnum;
//import com.minnovation.common.exception.BusinessException;
//import com.minnovation.common.transaction.CommonTransactionExecutor;
//import com.minnovation.common.utils.FileUtils;
//import com.minnovation.common.utils.MyBusinessValidator;
//import com.minnovation.dao.FileDao;
//import com.minnovation.entities.FileEntity;
//import com.minnovation.helper.FileHelper;
//import com.minnovation.request.FileRequest;
//import com.minnovation.service.FileService;
//import com.minnovation.service.store.FileStoreService;
//import com.minnovation.vo.FileVO;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.io.FilenameUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.io.InputStream;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * 文件服务
// *
// * @author liuyang
// */
//@Slf4j
//@Service
//public class FileServiceImpl implements FileService {
//
//    @Value("${aws.bucket.biz}")
//    private String bucketName;
//
//    @Value("${aws.tmp.path}")
//    private String tmpPath;
//
//    @Resource
//    private CommonTransactionExecutor commonTransactionExecutor;
//
//    public static final Set<String> TYPE_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
//            "pdf",
//            "docx",
//            "doc",
//            "xlsx",
//            "png",
//            "jpg",
//            "jpeg",
//            "tiff",
//            "gif",
//            "heic",
//            "webp"
//    )
//    ));
//
//    public static final Set<String> PREVIEW_TYPE_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
//            "png",
//            "jpg",
//            "jpeg",
//            "tiff",
//            "gif",
//            "webp")
//    ));
//
//    @Resource
//    private FileDao fileDao;
//
//    @Resource
//    private FileStoreService fileStoreService;
//
//    @Override
//    public FileVO create(MultipartFile rawFile, long uid) {
//        try (InputStream inputStream = rawFile.getInputStream()) {
//            String md5;
//            if (inputStream.markSupported()) {
//                inputStream.mark(0);
//                md5 = DigestUtils.md5Hex(inputStream);
//                inputStream.reset();
//            } else {
//                md5 = multipartMd5(rawFile);
//            }
//
//            return create(inputStream, md5, rawFile.getOriginalFilename(), uid);
//        } catch (IOException e) {
//            log.error("create file error:{}", rawFile.getOriginalFilename(), e);
//        }
//        return null;
//    }
//
//    private String multipartMd5(MultipartFile multipartFile) throws IOException {
//        try (InputStream inputStream = multipartFile.getInputStream()) {
//            return DigestUtils.md5Hex(inputStream);
//        }
//    }
//
//    @Override
//    public FileVO create(InputStream inputStream, String md5, String fileName, long uid) {
//        // File
//        try {
//            // 文件存在md5
//            FileVO fileVO = exist(md5, uid);
//            if (fileVO != null) {
//                fileVO.setName(fileName);
//                fileVO.setExisted(true);
//                log.info("upload exist file:{},{}", md5, fileName);
//                return fileVO;
//            }
//            String type = FilenameUtils.getExtension(fileName);
//            boolean notSupportedType = type == null || !TYPE_SET.contains(type.toLowerCase());
//            MyBusinessValidator.validate(notSupportedType, ErrorCodeEnum.SYSTEM_ERROR, ErrorMessageEnum.PARAMETER_ERROR);
//            FileEntity fileEntity = new FileEntity();
//            fileEntity.setName(fileName);
//            fileEntity.setType(type);
//            fileEntity.setMd5(md5);
//            String key = UUID.randomUUID().toString();
//            String path = fileStoreService.upload(inputStream, bucketName, key + "." + type.toLowerCase());
//            fileEntity.setPath(path);
//            fileEntity.setCreatedBy(uid);
//            fileEntity.setCreatedAt(LocalDateTime.now());
//            fileEntity.setModifiedAt(LocalDateTime.now());
//            commonTransactionExecutor.doTrans(() -> fileDao.create(fileEntity));
//
//            fileVO = FileHelper.convertToFileVO(fileEntity);
//            FileUtils.encryptFileVO(fileVO, uid);
//            return fileVO;
//        } catch (BusinessException e) {
//            log.info("met BusinessException:{},uid:{}", fileName, uid);
//            throw e;
//        } catch (Exception e) {
//            log.error("FileServiceImpl.create 文件系统异常,{},uid:{}", fileName, uid, e);
//            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, ErrorMessageEnum.FILE_ERROR);
//        }
//    }
//
////    public FileVO generateDownloadUrl(long id) {
////        File file = fileDao.get(id);
////        MyBusinessValidator.validate(file == null, ErrorCodeEnum.SYSTEM_ERROR, ErrorMessageEnum.FILE_NOT_EXIST);
////        String url = fileStoreService.generateDownloadUrl(bucketName, file.getS3Path(), 120);
////        FileVO fileVO = FileHelper.convertToFileVO(file);
////        fileVO.setUrl(url);
////        return fileVO;
////    }
//
//    @Override
//    public FileVO exist(String md5, long uid) {
//        FileEntity fileEntity = fileDao.findByMd5(md5);
//        if (fileEntity == null) {
//            return null;
//        }
//        FileVO fileVO = FileHelper.convertToFileVO(fileEntity);
//        FileUtils.encryptFileVO(fileVO, uid);
//        return fileVO;
//    }
//
//    @Override
//    public FileVO download(FileEncrypt fileEncrypt, long uid) {
//        FileUtils.validateKey(fileEncrypt, uid);
//        FileEntity fileEntity = fileDao.get(fileEncrypt.getFileId());
//        MyBusinessValidator.validate(fileEntity == null, ErrorCodeEnum.SYSTEM_ERROR, ErrorMessageEnum.FILE_NOT_EXIST);
//        String path = fileStoreService.download(bucketName, fileEntity.getPath(), tmpPath);
//        FileVO fileVO = FileHelper.convertToFileVO(fileEntity);
//        fileVO.setLocalPath(path);
//        return fileVO;
//    }
//
//    /**
//     * 文件VO转换
//     */
//    @Override
//    public List<FileVO> convertToFileVO(List<FileRequest> requestList, long uid) {
//        if (requestList == null || requestList.isEmpty()) {
//            return Collections.emptyList();
//        }
//        // 增加文件验证
//        requestList.forEach(fileRequest -> FileUtils.validateKey(fileRequest.getFileEncrypt(), uid));
//        List<Long> fileIds = requestList.stream().map(FileRequest::getId).collect(Collectors.toList());
//        List<FileEntity> fileEntities = fileDao.get(fileIds);
//        Map<Long, FileEntity> fileEntityMap = fileEntities.stream().collect(Collectors.toMap(FileEntity::getId, fileEntity -> fileEntity));
//        // 转换输出文件
//        List<FileVO> res = new ArrayList<>();
//        for (FileRequest request : requestList) {
//            if (fileEntityMap.containsKey(request.getId())) {
//                FileEntity fileEntity = fileEntityMap.get(request.getId());
//                FileVO fileVO = FileHelper.convertToFileVO(fileEntity);
//                // 设置文件名
//                fileVO.setName(request.getName());
//                res.add(fileVO);
//            }
//        }
//        FileUtils.encryptFileVOList(res, uid);
//        return res;
//    }
//}
