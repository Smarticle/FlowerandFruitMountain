package com.cetc36.demo.base.store;

import java.io.InputStream;

public interface FileStoreService {
    /**
     * @param inputStream     input file stream
     * @param targetContainer bucket for s3, or local path for local fs
     * @param key             dest file name, key for s3, file name for local fs
     * @return uploaded file key
     */
    String upload(InputStream inputStream, String targetContainer, String key);

    /**
     * download file by bucket or local fs and key,then save to local path with key name
     *
     * @param targetContainer bucket for s3, or local path for local fs
     * @param key             file name, key for s3, file name for local fs
     * @param savePath        save to local fs path
     * @return local file path of downloaded file
     */
    String download(String targetContainer, String key, String savePath);


//    /**
//     * get download url by bucket or local fs and key
//     *
//     * @param targetContainer bucket for s3, or local path for local fs
//     * @param key             file name, key for s3, file name for local fs
//     * @param expiredTime     expired time minutes
//     * @return local file path of downloaded file
//     */
//    String generateDownloadUrl(String targetContainer, String key, int expiredTime);
}
