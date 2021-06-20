package com.bhtcnpm.website.service.Doc;

import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface DocViewService {
    boolean addDocView (Long docID, UUID userID, String ipAddress);
    boolean addDocView (Long docID, Authentication authentication, String ipAddress);
}
