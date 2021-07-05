package com.bhtcnpm.website.model.dto.DocComment;

import com.bhtcnpm.website.model.entity.DocCommentEntities.DocComment;
import com.bhtcnpm.website.repository.Doc.DocRepository;
import com.bhtcnpm.website.repository.Doc.DocCommentRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mapper
public abstract class DocCommentMapper {
    public static final DocCommentMapper INSTANCE = Mappers.getMapper(DocCommentMapper.class);

    protected DocCommentRepository docCommentRepository;
    protected DocRepository docRepository;
    protected UserWebsiteRepository userWebsiteRepository;

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "authorAvatarURL", source = "author.avatarURL")
    @Mapping(target = "childCommentCount", ignore = true)
    public abstract DocCommentDTO docCommentToDocCommentDTO(DocComment docComment);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "authorAvatarURL", source = "author.avatarURL")
    public abstract DocCommentChildDTO docCommentToDocCommentChildDTO(DocComment docComment);

    public DocCommentListDTO docCommentPageToDocCommentListDTO (Page<DocCommentDTO> docCommentDTOs) {
        return DocCommentListDTO.builder()
                .docCommentDTOs(docCommentDTOs.getContent())
                .totalPages(docCommentDTOs.getTotalPages())
                .totalElements(docCommentDTOs.getTotalElements())
                .build();
    }

    public DocComment docCommentDTOToDocComment (DocCommentRequestDTO docCommentRequestDTO,
                                                 Long docID, Long parentCommentID, UUID authorID, DocComment entity) {
        DocComment docComment = Objects.requireNonNullElseGet(entity, DocComment::new);
        if (docID == null || docCommentRequestDTO == null || authorID == null) {
            throw new IllegalArgumentException("DocID, DocCommentRequestDTO and authorID cannot be null");
        }

        if (parentCommentID != null) {
            docComment.setParentComment(docCommentRepository.getOne(parentCommentID));
        } else {
            docComment.setParentComment(null);
        }

        docComment.setDoc(docRepository.getOne(docID));
        docComment.setAuthor(userWebsiteRepository.getOne(authorID));
        docComment.setContent(docCommentRequestDTO.getContent());
        //TODO: Do XSS filter here.

        return docComment;
    }

    public abstract List<DocCommentChildDTO> docCommentListToDocCommentChildDTOList (List<DocComment> docComments);

    public abstract List<DocCommentDTO> docCommentListToDocCommentDTOListChildCommentOnly (List<DocComment> docComments);

    @Autowired
    public void setDocCommentRepository (DocCommentRepository docCommentRepository) {
        this.docCommentRepository = docCommentRepository;
    }

    @Autowired
    public void setDocRepository (DocRepository docRepository) {this.docRepository = docRepository;}

    @Autowired
    public void setUserWebsiteRepository (UserWebsiteRepository userWebsiteRepository) {this.userWebsiteRepository = userWebsiteRepository;}
}
