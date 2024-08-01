package com.example.Platform.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String storeFile(MultipartFile file) throws IOException;
    boolean isImageFile(MultipartFile file);
    void deleteFile(String filePath) throws IOException;
}
