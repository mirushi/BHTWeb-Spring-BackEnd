package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DocSummaryMapper {

    DocSummaryMapper INSTANCE = Mappers.getMapper(DocSummaryMapper.class);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "subject", source = "subject.name")
    @Mapping(target = "subjectID", source = "subject.id")
    @Mapping(target = "downloadCount", source = "docFileUpload.downloadCount")
    @Mapping(target = "viewCount", source = "viewCount")
    DocSummaryDTO docToDocSummaryDTO (Doc doc);

    List<DocSummaryDTO> docListToDocSummaryDTOList (List<Doc> docList);

    @Mapping(target = "docSummary", source = ".")
    @Mapping(target = "docState", source = "docState")
    DocSummaryWithStateDTO docToDocSummaryWithStateDTO (Doc doc);

    List<DocSummaryWithStateDTO> docListToDocSummaryWithStateDTOList (List<Doc> docList);
}
