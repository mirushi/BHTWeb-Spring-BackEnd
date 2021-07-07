package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.constant.business.Doc.AllowedUploadExtension;
import com.bhtcnpm.website.model.dto.AWS.AmazonS3ResultDTO;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.bhtcnpm.website.service.AmazonS3Service;
import com.bhtcnpm.website.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.jsoup.helper.Validate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final AmazonS3Service amazonS3Service;
    private final Tika tika = new Tika();

    @Override
    public String uploadToGoogleDrive(MultipartFile multipartFile) {
        return null;
    }

    @Override
    public AmazonS3ResultDTO uploadImageToS3(String key, MultipartFile multipartFile) throws IOException, FileExtensionNotAllowedException {
        //TODO: Limit maximum size.
        long contentByteLength = multipartFile.getSize();
        String detectedContentType = tika.detect(multipartFile.getBytes());
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        String contentType = AllowedUploadExtension.getImageMimeType(extension);

        Validate.isTrue(detectedContentType.equals(contentType));

        return amazonS3Service.uploadFile(key, contentByteLength, contentType, multipartFile);
    }
}
