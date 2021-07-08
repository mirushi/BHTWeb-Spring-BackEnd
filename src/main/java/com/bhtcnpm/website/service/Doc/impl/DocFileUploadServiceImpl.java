package com.bhtcnpm.website.service.Doc.impl;

import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.repository.Doc.DocFileUploadRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Doc.DocFileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DocFileUploadServiceImpl implements DocFileUploadService {

    private final DocFileUploadRepository docFileUploadRepository;

    @Override
    public List<DocFileUpload> filterFileUploadForDoc (List<UUID> docFileIDList, Long docID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        List<DocFileUpload> fileUploadList = docFileUploadRepository.filterDocFileUpload(docFileIDList, docID, userID);
        if (fileUploadList.size() != docFileIDList.size()){
            throw new IllegalArgumentException("Some of the file(s) could not be found, not yours or has already been assigned to other Doc.");
        }

        return fileUploadList;
    }
}
