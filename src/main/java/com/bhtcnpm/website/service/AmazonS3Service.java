package com.bhtcnpm.website.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bhtcnpm.website.model.dto.AWS.AmazonS3ResultDTO;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AmazonS3Service {
    private final String bucketName;
    private final AmazonS3Client amazonS3Client;
    private String bucketLocation;

    public AmazonS3Service(@Value("${custom.bucket-name}") String bucketName, AmazonS3Client amazonS3Client) {
        this.bucketName = bucketName;
        this.amazonS3Client = amazonS3Client;
    }

    @PostConstruct
    public void postConstruct() {
        this.bucketLocation = String.format("https://%s.s3.%s.amazonaws.com",
                bucketName, this.amazonS3Client.getBucketLocation(bucketName));
    }

    public AmazonS3ResultDTO uploadFile (String key, long contentLengthByte, String contentType, MultipartFile multipartFile) throws IOException {
        String md5Base64 = "";
        try {
            byte[] resultByte = DigestUtils.md5(multipartFile.getInputStream());
            md5Base64 = new String(Base64.encodeBase64(resultByte));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(contentLengthByte);
        meta.setContentType(contentType);
        meta.setContentMD5(md5Base64);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, multipartFile.getInputStream(), meta);

        amazonS3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));

        return AmazonS3ResultDTO.builder()
                .directURL(bucketLocation + "/" + key)
                .build();
    }
}
