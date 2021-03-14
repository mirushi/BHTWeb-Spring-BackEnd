package com.bhtcnpm.website.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadToGoogleDrive(MultipartFile multipartFile);
}
