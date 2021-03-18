package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DocSummaryMapper {

    DocSummaryMapper INSTANCE = Mappers.getMapper(DocSummaryMapper.class);

    @Mapping(source = "author.id", target = "authorID")
    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "category.id", target = "categoryID")
    @Mapping(source = "subject.name", target = "docSubject")
    @Mapping(source = "subject.id", target = "docSubjectID")
    @Mapping(source = "downloadCount", target = "downloads")
    @Mapping(source = "publishDtm", target = "publishDtm")
    @Mapping(source = "viewCount", target = "views")
    DocSummaryDTO docToDocSummaryDTO (Doc doc);

}
