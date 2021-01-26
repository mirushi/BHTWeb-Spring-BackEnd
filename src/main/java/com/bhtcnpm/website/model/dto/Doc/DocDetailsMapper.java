package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.dto.Tag.TagMapper;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TagMapper.class})
public interface DocDetailsMapper {

    DocDetailsMapper INSTANCE = Mappers.getMapper(DocDetailsMapper.class);

    @Mapping(source = "author.id", target = "authorID")
    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "category.id", target = "categoryID")
    @Mapping(source = "subject.name", target = "docSubject")
    @Mapping(source = "subject.id", target = "docSubjectID")
    @Mapping(source = "downloadCount", target = "downloads")
    @Mapping(source = "publishDtm", target = "publishDtm")
    @Mapping(source = "viewCount", target = "views")
    DocDetailsDTO docToDocDetailsDTO (Doc doc);

}
