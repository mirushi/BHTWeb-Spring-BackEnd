package com.bhtcnpm.website.model.dto.Doc.mapper;

import com.bhtcnpm.website.model.dto.Doc.DocRequestDTO;
import com.bhtcnpm.website.model.dto.Tag.TagMapper;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.repository.Doc.DocCategoryRepository;
import com.bhtcnpm.website.repository.Doc.DocFileUploadRepository;
import com.bhtcnpm.website.repository.Subject.SubjectRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mapper
public abstract class DocRequestMapper {

    public static final DocRequestMapper INSTANCE = Mappers.getMapper(DocRequestMapper.class);

    protected DocCategoryRepository docCategoryRepository;

    protected SubjectRepository subjectRepository;

    protected UserWebsiteRepository userWebsiteRepository;

    protected DocFileUploadRepository docFileUploadRepository;

    protected TagMapper tagMapper;

    protected DocFileUploadMapper docFileUploadMapper;

    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "subjectID", source = "subject.id")
    public abstract DocRequestDTO docToDocRequestDTO (Doc doc);

    public Doc updateDocFromDocRequestDTO (DocRequestDTO docRequestDTO, Doc entity , List<DocFileUpload> docFileUploadList, UUID userID) {
        Doc newDoc = Objects.requireNonNullElseGet(entity, Doc::new);

        if (docRequestDTO == null) {
            return entity;
        }

        if (entity == null) {
            newDoc.setSubmitDtm(LocalDateTime.now());
            newDoc.setDocState(DocStateType.PENDING_APPROVAL);
            newDoc.setAuthor(userWebsiteRepository.getOne(userID));
            newDoc.setVersion((short)0);
        }
        newDoc.setDocFileUploads(docFileUploadList);
        newDoc.setLastUpdatedDtm(LocalDateTime.now());
        newDoc.setLastEditedUser(userWebsiteRepository.getOne(userID));
        newDoc.setCategory(docCategoryRepository.getOne(docRequestDTO.getCategoryID()));
        newDoc.setSubject(subjectRepository.getOne(docRequestDTO.getSubjectID()));
        newDoc.setTitle(docRequestDTO.getTitle());
        newDoc.setDescription(docRequestDTO.getDescription());
        if (docRequestDTO.getImageURL() == null && docFileUploadList != null && !docFileUploadList.isEmpty() && docFileUploadList.get(1) != null) {
            String firstFileThumbnail = docFileUploadList.get(1).getThumbnailURL();
            newDoc.setImageURL(firstFileThumbnail);
        } else {
            newDoc.setImageURL(docRequestDTO.getImageURL());
        }
        newDoc.setTags(tagMapper.tagDTOListToTagList(docRequestDTO.getTags()));
        if (newDoc.getPublishDtm() == null) {
            if (docRequestDTO.getPublishDtm() == null || docRequestDTO.getPublishDtm().isBefore(LocalDateTime.now())) {
                newDoc.setPublishDtm(LocalDateTime.now());
            } else {
                newDoc.setPublishDtm(docRequestDTO.getPublishDtm());
            }
        }

        return newDoc;
    }

    @Autowired
    public void setDocCategoryRepository (DocCategoryRepository docCategoryRepository) {
        this.docCategoryRepository = docCategoryRepository;
    }

    @Autowired
    public void setSubjectRepository (SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Autowired
    public void setTagMapper (TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Autowired
    public void setUserWebsiteRepository (UserWebsiteRepository userWebsiteRepository) {
        this.userWebsiteRepository = userWebsiteRepository;
    }

    @Autowired
    public void setDocFileUploadRepository (DocFileUploadRepository docFileUploadRepository) {
        this.docFileUploadRepository = docFileUploadRepository;
    }

    @Autowired
    public void setDocFileUploadMapper (DocFileUploadMapper docFileUploadMapper) {
        this.docFileUploadMapper = docFileUploadMapper;
    }

//    @Mapping(target = "lastUpdatedDtm", expression = "java(java.time.LocalDateTime.now())")
//    @Mapping(target = "lastEditedUser.id", source = "lastEditedUserID")
//    @Mapping(target = "category", source = "docRequestDTO.categoryID")
//    @Mapping(target = "subject", source = "docRequestDTO.docSubjectID")
//    Doc updateDocFromDocRequestDTO (Long lastEditedUserID, DocRequestDTO docRequestDTO, @MappingTarget Doc entity);
}
