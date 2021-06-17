package com.bhtcnpm.website.model.dto.Doc.mapper;

import com.bhtcnpm.website.model.dto.Doc.DocSummaryDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryWithStateDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DocSummaryMapper {

    DocSummaryMapper INSTANCE = Mappers.getMapper(DocSummaryMapper.class);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "subjectID", source = "subject.id")
    @Mapping(target = "subjectName", source = "subject.name")
    DocSummaryDTO docToDocSummaryDTO (Doc doc);

    List<DocSummaryDTO> docListToDocSummaryDTOList (List<Doc> docList);

    @Mapping(target = "docSummary", source = ".")
    @Mapping(target = "docState", source = "docState")
    DocSummaryWithStateDTO docToDocSummaryWithStateDTO (Doc doc);

    List<DocSummaryWithStateDTO> docListToDocSummaryWithStateDTOList (List<Doc> docList);
}
