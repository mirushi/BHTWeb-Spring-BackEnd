package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.AWS.AmazonS3ResultDTO;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String uploadToGoogleDrive(MultipartFile multipartFile);
    AmazonS3ResultDTO uploadImageToS3 (String key, MultipartFile multipartFile) throws IOException, FileExtensionNotAllowedException;
}
