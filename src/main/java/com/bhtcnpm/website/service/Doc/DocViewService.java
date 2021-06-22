package com.bhtcnpm.website.service.Doc;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface DocViewService {
    @PreAuthorize(value = "permitAll()")
    boolean addDocView (Long docID, UUID userID, String ipAddress);

    @PreAuthorize(value = "permitAll()")
    boolean addDocView (Long docID, Authentication authentication, String ipAddress);
}
