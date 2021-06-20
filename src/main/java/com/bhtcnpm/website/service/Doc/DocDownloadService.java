package com.bhtcnpm.website.service.Doc;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface DocDownloadService {
    @PreAuthorize(value = "permitAll()")
    boolean addDocDownload (UUID docFileUploadID, UUID userID, String ipAddress);

    @PreAuthorize(value = "permitAll()")
    boolean addDocDownload (UUID docFileUploadID, Authentication authentication, String ipAddress);
}
