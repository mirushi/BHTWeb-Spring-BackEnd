package com.bhtcnpm.website.model.dto.DocSubject;

import com.bhtcnpm.website.model.entity.DocEntities.DocSubject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DocSubjectMapper {

    DocSubjectMapper INSTANCE = Mappers.getMapper(DocSubjectMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "docs", ignore = true)
    @Mapping(target = "version", ignore = true)
    DocSubject docSubjectDTOToDocSubject(DocSubjectDTO docCategoryDTO, @MappingTarget DocSubject docCategory);

    DocSubjectDTO docSubjectToDocSubjectDTO (DocSubject docSubject);

    List<DocSubjectDTO> docSubjectListToDocSubjectDTOList (List<DocSubject> docCategories);

}
