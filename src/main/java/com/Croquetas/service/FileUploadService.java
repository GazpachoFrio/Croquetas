package com.Croquetas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${upload.path}")
    private String uploadPath;

    public String saveRecipePhoto(MultipartFile file) throws IOException {
        // Create directory if not exists
        Path directory = Paths.get(uploadPath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Generate unique filename to avoid collisions
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;

        Path filePath = directory.resolve(newFileName);
        Files.write(filePath, file.getBytes());

        // Return the relative URL path to access the image
        return "/uploads/" + newFileName;
    }
    public String saveProfilePhoto(MultipartFile file) throws IOException {
        Path directory = Paths.get(uploadPath + "profiles/");
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;
        Path filePath = directory.resolve(newFileName);
        Files.write(filePath, file.getBytes());
        return "/uploads/profiles/" + newFileName;
    }
}