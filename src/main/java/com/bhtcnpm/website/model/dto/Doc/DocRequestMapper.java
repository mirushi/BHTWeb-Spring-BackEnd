package com.bhtcnpm.website.model.dto.Doc;

import com.bhtcnpm.website.model.entity.Doc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocRequestMapper {

    DocRequestMapper INSTANCE = Mappers.getMapper(DocRequestMapper.class);

    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "docSubjectID", source = "subject.id")
    DocRequestDTO docToDocRequestDTO (Doc doc);

    @Mapping(target = "lastEditDtm", expression = "java(java.time.LocalDateTime.now())")
    Doc updateDocFromDocRequestDTO (Long authorID, DocRequestDTO docRequestDTO, @MappingTarget Doc entity);
}
