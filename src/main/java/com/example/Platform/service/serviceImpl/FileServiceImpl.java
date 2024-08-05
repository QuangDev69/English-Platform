package com.example.Platform.service.serviceImpl;

import com.example.Platform.service.FileService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private final Path rootLocation = Paths.get("uploads");

    @Override
    @Transactional
    public String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = UUID.randomUUID() + "_" + filename;
        java.nio.file.Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @Override
    public boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @Override
    @Transactional
    public void deleteFile(String filePath) throws IOException {
        Path path = rootLocation.resolve(filePath).normalize().toAbsolutePath();
        System.out.println("Attempting to delete file at: " + path);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
                System.out.println("File deleted successfully: " + path);
            } catch (IOException e) {
                System.err.println("Failed to delete file: " + path);
                e.printStackTrace();
                throw new IOException("Failed to delete file: " + filePath, e);
            }
        } else {
            System.err.println("File not found: " + path);
            throw new IOException("File not found: " + filePath);
        }
    }
}
