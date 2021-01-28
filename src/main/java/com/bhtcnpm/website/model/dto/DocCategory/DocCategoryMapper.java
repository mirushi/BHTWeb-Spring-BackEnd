package com.bhtcnpm.website.model.dto.DocCategory;

import com.bhtcnpm.website.model.entity.DocEntities.DocCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DocCategoryMapper {

    DocCategoryMapper INSTANCE = Mappers.getMapper(DocCategoryMapper.class);

    @Mapping(target = "id", ignore = true)
    DocCategory docCategoryDTOToDocCategory(DocCategoryDTO docCategoryDTO, @MappingTarget DocCategory docCategory);

    DocCategoryDTO docCategoryToDocCategoryDTO (DocCategory docCategory);

    List<DocCategoryDTO> docCategoryListToDocCategoryDTOList (List<DocCategory> docCategories);

}
