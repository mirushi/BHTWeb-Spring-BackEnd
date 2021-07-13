package com.bhtcnpm.website.service.Doc.impl;

import com.bhtcnpm.website.model.entity.DocEntities.DocDownload;
import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.repository.Doc.DocDownloadRepository;
import com.bhtcnpm.website.repository.Doc.DocFileUploadRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Doc.DocDownloadService;
import com.bhtcnpm.website.service.Doc.DocService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DocDownloadServiceImpl implements DocDownloadService {

    private final DocDownloadRepository docDownloadRepository;
    private final DocFileUploadRepository docFileUploadRepository;
    private final UserWebsiteRepository uwRepository;

    private final DocService docService;

    @Override
    public boolean addDocDownload(UUID docFileUploadID, UUID userID, String ipAddress) {
        boolean isUserDownloadedDoc = docDownloadRepository.existsByDocIdAndUserIdOrIpAddress(docFileUploadID, userID, ipAddress);
        if (isUserDownloadedDoc) {
            return false;
        }

        Optional<DocFileUpload> docFileUploadOpt = docFileUploadRepository.findById(docFileUploadID);
        Validate.isTrue(docFileUploadOpt.isPresent());

        DocFileUpload docFileUpload = docFileUploadOpt.get();

        UserWebsite user = null;
        if (userID != null) {
            user = uwRepository.getOne(userID);
        }


        DocDownload docDownload = DocDownload.builder()
                .docFileUpload(docFileUpload)
                .user(user)
                .ipAddress(ipAddress)
                .build();

        docDownloadRepository.save(docDownload);

        docService.updateDownloads(docFileUpload.getDoc().getId());

        return true;
    }

    @Override
    public boolean addDocDownload(UUID docFileUploadID, Authentication authentication, String ipAddress) {
        UUID userID = SecurityUtils.getUserID(authentication);

        return addDocDownload(docFileUploadID, userID, ipAddress);
    }
}
