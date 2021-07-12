package com.bhtcnpm.website.util;

import com.amazonaws.services.s3.AmazonS3;
import com.bhtcnpm.website.constant.api.amazon.s3.AmazonS3Constant;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class FileUploadUtils {

    public static String getFileExtensionFromMultipart (MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    public static String getS3AvatarURLUploadKey (UUID userID, MultipartFile file) {
        return AmazonS3Constant.avatarUploadKey + userID + "-" + UUID.randomUUID() + "." + getFileExtensionFromMultipart(file);
    }

    public static String getS3PostImageURLUploadKey (UUID userID, MultipartFile file) {
        return AmazonS3Constant.postImageUploadKey + userID + "-" + UUID.randomUUID() + "." + getFileExtensionFromMultipart(file);
    }

    public static String getS3DocImageURLUploadKey (UUID userID, MultipartFile file) {
        return AmazonS3Constant.docImageUploadKey + userID + "-" + UUID.randomUUID() + "." + getFileExtensionFromMultipart(file);
    }
}
