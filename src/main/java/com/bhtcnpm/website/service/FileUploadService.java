package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.AWS.AmazonS3ResultDTO;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface FileUploadService {
    com.google.api.services.drive.model.File uploadToGoogleDrive(MultipartFile multipartFile, String folderID, Authentication authentication) throws FileExtensionNotAllowedException, IOException;
    AmazonS3ResultDTO uploadImageToS3 (String key, MultipartFile multipartFile) throws IOException, FileExtensionNotAllowedException;
}
