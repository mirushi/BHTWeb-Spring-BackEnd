package com.bhtcnpm.website.model.dto.Doc.mapper;

import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.repository.DocFileUploadRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Mapper
public abstract class DocFileUploadMapper {

    protected DocFileUploadRepository docFileUploadRepository;

    public DocFileUpload docFileUploadIDToDocFileUpload (UUID id) {
        if (id == null) {
            return null;
        }
        return docFileUploadRepository.getOne(id);
    }

    public List<DocFileUpload> docFileUploadIDListToDocFileUpload (List<UUID> idList) {
        return docFileUploadRepository.findAllById(idList);
    }

    @Autowired
    public void setDocFileUploadRepository (DocFileUploadRepository docFileUploadRepository) {
        this.docFileUploadRepository = docFileUploadRepository;
    }
}
