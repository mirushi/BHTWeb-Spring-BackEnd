package com.bhtcnpm.website.service.Doc.impl;

import com.bhtcnpm.website.model.entity.DocEntities.DocView;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.repository.Doc.DocRepository;
import com.bhtcnpm.website.repository.Doc.DocViewRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Doc.DocViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DocViewServiceImpl implements DocViewService {

    private final DocViewRepository docViewRepository;
    private final DocRepository docRepository;
    private final UserWebsiteRepository uwRepository;

    @Override
    public boolean addDocView(Long docID, UUID userID, String ipAddress) {
        boolean isUserViewedDoc = docViewRepository.existsByDocIdAndUserIdOrIpAddress(docID, userID, ipAddress);
        if (isUserViewedDoc) {
            return false;
        }

        DocView docView = new DocView();
        docView.setDoc(docRepository.getOne(docID));
        if (userID != null) {
            docView.setUser(uwRepository.getOne(userID));
        }
        docView.setIpAddress(ipAddress);

        docViewRepository.save(docView);

        return true;
    }

    @Override
    public boolean addDocView(Long docID, Authentication authentication, String ipAddress) {
        UUID userID = SecurityUtils.getUserID(authentication);

        return addDocView(docID, userID, ipAddress);
    }
}
