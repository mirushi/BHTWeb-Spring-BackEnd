package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.Tag.TagMapper;
import com.bhtcnpm.website.model.entity.Doc;
import com.bhtcnpm.website.model.entity.DocCategory;
import com.bhtcnpm.website.model.entity.DocSubject;
import com.bhtcnpm.website.repository.DocCategoryRepository;
import com.bhtcnpm.website.repository.DocRepository;
import com.bhtcnpm.website.repository.DocSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Mapper
public abstract class DocRequestMapper {

    public static final DocRequestMapper INSTANCE = Mappers.getMapper(DocRequestMapper.class);

    protected DocCategoryRepository docCategoryRepository;

    protected DocSubjectRepository docSubjectRepository;

    protected TagMapper tagMapper;

    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "subjectID", source = "subject.id")
    public abstract DocRequestDTO docToDocRequestDTO (Doc doc);

    public Doc updateDocFromDocRequestDTO (Long lastEditedUserID, DocRequestDTO docRequestDTO, Doc entity) {
        Doc newDoc;

        if (entity == null) {
            newDoc = new Doc();
        } else {
            newDoc = entity;
        }

        if (docRequestDTO == null) {
            return entity;
        }

        if (docRequestDTO.getCategoryID() != null) {
            newDoc.setCategory(docCategoryRepository.getOne(docRequestDTO.getCategoryID()));
        } else {
            newDoc.setCategory(null);
        }
        newDoc.setCategory(docCategoryRepository.getOne(docRequestDTO.getCategoryID()));
        newDoc.setSubject(docSubjectRepository.getOne(docRequestDTO.getSubjectID()));
        newDoc.setTitle(docRequestDTO.getTitle());
        newDoc.setDescription(docRequestDTO.getDescription());
        newDoc.setImageURL(docRequestDTO.getImageURL());
        newDoc.setDocURL(docRequestDTO.getDocURL());
        newDoc.setTags(tagMapper.tagDTOListToTagList(docRequestDTO.getTags()));
        newDoc.setPublishDtm(docRequestDTO.getPublishDtm());
        newDoc.setVersion(docRequestDTO.getVersion());

        return newDoc;
    }

    @Autowired
    public void setDocCategoryRepository (DocCategoryRepository docCategoryRepository) {
        this.docCategoryRepository = docCategoryRepository;
    }

    @Autowired
    public void setDocSubjectRepository (DocSubjectRepository docSubjectRepository) {
        this.docSubjectRepository = docSubjectRepository;
    }

    @Autowired
    public void setTagMapper (TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

//    @Mapping(target = "lastEditDtm", expression = "java(java.time.LocalDateTime.now())")
//    @Mapping(target = "lastEditedUser.id", source = "lastEditedUserID")
//    @Mapping(target = "category", source = "docRequestDTO.categoryID")
//    @Mapping(target = "subject", source = "docRequestDTO.docSubjectID")
//    Doc updateDocFromDocRequestDTO (Long lastEditedUserID, DocRequestDTO docRequestDTO, @MappingTarget Doc entity);
}
