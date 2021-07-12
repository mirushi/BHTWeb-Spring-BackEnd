package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.constant.business.Doc.AllowedUploadExtension;
import com.bhtcnpm.website.model.dto.AWS.AmazonS3ResultDTO;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.AmazonS3Service;
import com.bhtcnpm.website.service.FileUploadService;
import com.bhtcnpm.website.service.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.jsoup.helper.Validate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final AmazonS3Service amazonS3Service;
    private final Tika tika = new Tika();

    @Override
    public com.google.api.services.drive.model.File uploadToGoogleDrive(MultipartFile multipartFile, String folderID, Authentication authentication) throws FileExtensionNotAllowedException, IOException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        byte[] fileContent = multipartFile.getBytes();
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        //Get mime type.
        String mimeType = AllowedUploadExtension.getMimeType(extension);

        //Use tika to determine type.
        String detectedContentType = tika.detect(multipartFile.getBytes());

        Validate.isTrue(detectedContentType.equals(mimeType));

        //TODO: Generate random file name (for security reason, don't trust user submitted file name.
        //https://cheatsheetseries.owasp.org/cheatsheets/File_Upload_Cheat_Sheet.html#file-upload-threats
//        String fileName = RandomStringUtils.randomAlphanumeric(FILE_NAME_RANDOM_LENGTH) + "." + extension;
        String fileName = multipartFile.getOriginalFilename();

        //Get subfolder of userID to upload file.
        com.google.api.services.drive.model.File uploadedFile = GoogleDriveService
                .createGoogleFileWithUserID(folderID, userID, mimeType, fileName, fileContent);

        return uploadedFile;
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
