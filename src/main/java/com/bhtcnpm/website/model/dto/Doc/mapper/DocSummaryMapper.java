package com.bhtcnpm.website.model.dto.Doc.mapper;

import com.bhtcnpm.website.model.dto.Doc.DocSummaryDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryListDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryWithStateAndFeedbackDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryWithStateDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocSave;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public abstract class DocSummaryMapper {

    public static final DocSummaryMapper INSTANCE = Mappers.getMapper(DocSummaryMapper.class);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "authorAvatarURL", source = "author.avatarURL")
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "subjectID", source = "subject.id")
    @Mapping(target = "subjectName", source = "subject.name")
    public abstract DocSummaryDTO docToDocSummaryDTO (Doc doc);

    public abstract List<DocSummaryDTO> docListToDocSummaryDTOList (List<Doc> docList);

    @Mapping(target = "docSummary", source = ".")
    @Mapping(target = "docState", source = "docState")
    public abstract DocSummaryWithStateDTO docToDocSummaryWithStateDTO (Doc doc);

    @Mapping(target = "authorAvatarURL", source = "doc.author.avatarURL")
    @Mapping(target = "authorDisplayName", source = "doc.author.displayName")
    @Mapping(target = "authorID", source = "doc.author.id")
    @Mapping(target = "categoryID", source = "doc.category.id")
    @Mapping(target = "categoryName", source = "doc.category.name")
    @Mapping(target = "feedback", source = "adminFeedback")
    @Mapping(target = "subjectID", source = "subject.id")
    @Mapping(target = "subjectName", source = "subject.name")
    public abstract DocSummaryWithStateAndFeedbackDTO docToDocSummaryWithStateAndFeedback (Doc doc);

    public abstract List<DocSummaryWithStateAndFeedbackDTO> docListToDocSummaryWithStateAndFeedbackDTOList (List<Doc> docList);

    public abstract List<DocSummaryWithStateDTO> docListToDocSummaryWithStateDTOList (List<Doc> docList);

    public DocSummaryListDTO userDocSavePageToDocSummaryListDTO (Page<UserDocSave> userDocSaves) {
        List<Doc> docList = userDocSaves.getContent().stream().map(obj -> obj.getUserDocSaveId().getDoc()).collect(Collectors.toList());
        List<DocSummaryDTO> docSummaryDTOList = this.docListToDocSummaryDTOList(docList);

        return new DocSummaryListDTO(docSummaryDTOList, userDocSaves.getTotalPages(), userDocSaves.getTotalElements());
    }

}
