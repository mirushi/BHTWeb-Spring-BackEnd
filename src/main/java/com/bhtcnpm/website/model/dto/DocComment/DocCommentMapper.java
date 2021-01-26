package com.bhtcnpm.website.model.dto.DocComment;

import com.bhtcnpm.website.model.entity.DocComment;
import com.bhtcnpm.website.repository.DocCommentRepository;
import com.bhtcnpm.website.repository.DocRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Objects;

@Mapper
public abstract class DocCommentMapper {

    public static final DocCommentMapper INSTANCE = Mappers.getMapper(DocCommentMapper.class);

    protected DocCommentRepository docCommentRepository;
    protected UserWebsiteRepository userWebsiteRepository;
    protected DocRepository docRepository;

    @Mapping(target = "authorAvatarURL", source = "docComment.author.avatarURL")
    @Mapping(target = "authorID", source = "docComment.author.id")
    @Mapping(target = "authorName", source = "docComment.author.name")
    public abstract DocCommentDTO docCommentToDocCommentDTO (DocComment docComment);

    public abstract List<DocCommentDTO> docCommentListToDocCommentDTOList (List<DocComment> docComments);

    public DocCommentListDTO docCommentPageToDocCommentListDTO (Page<DocComment> docCommentPage) {
        DocCommentListDTO docCommentListDTO = new DocCommentListDTO(this.docCommentListToDocCommentDTOList(docCommentPage.getContent()), docCommentPage.getTotalPages());

        return docCommentListDTO;
    }

    public DocComment docCommentDTOToDocComment (DocCommentRequestDTO docCommentRequestDTO, Long authorID, Long docID, DocComment entity) {
        DocComment docComment = Objects.requireNonNullElseGet(entity, DocComment::new);

        if (docCommentRequestDTO == null) {
            return entity;
        }

        if (docCommentRequestDTO.getParentCommentID() != null) {
            docComment.setParentComment(docCommentRepository.getOne(docCommentRequestDTO.getParentCommentID()));
        } else {
            docComment.setParentComment(null);
        }

        if (authorID != null) {
            docComment.setAuthor(userWebsiteRepository.getOne(authorID));
        }

        if (docID != null) {
            docComment.setDoc(docRepository.getOne(docID));
        }

        docComment.setContent(docCommentRequestDTO.getContent());

        return docComment;
    }

    @Autowired
    public void setDocCommentRepository (DocCommentRepository docCommentRepository) {
        this.docCommentRepository = docCommentRepository;
    }

    @Autowired
    public void setUserWebsiteRepository (UserWebsiteRepository userWebsiteRepository) {
        this.userWebsiteRepository = userWebsiteRepository;
    }

    @Autowired
    public void setDocRepository (DocRepository docRepository) {
        this.docRepository = docRepository;
    }

}
