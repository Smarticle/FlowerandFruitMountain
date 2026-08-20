//package com.cetc36.demo.service;
//
//import com.cetc36.demo.request.FileRequest;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.InputStream;
//import java.util.List;
//
///**
// * 文件服务
// *
// * @author liuyang
// */
//public interface FileService {
//
//    FileVO create(MultipartFile file, long uid);
//
//    FileVO create(InputStream inputStream, String md5, String fileName, long uid);
//
//    FileVO exist(String md5, long uid);
//
//    FileVO download(FileEncrypt fileEncrypt, long uid);
//
//    List<FileVO> convertToFileVO(List<FileRequest> requestList, long uid);
//}
