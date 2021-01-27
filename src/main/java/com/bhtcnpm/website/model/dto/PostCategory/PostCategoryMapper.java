package com.bhtcnpm.website.model.dto.PostCategory;

import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostCategoryMapper {

    PostCategoryMapper INSTANCE = Mappers.getMapper(PostCategoryMapper.class);

    PostCategoryDTO postCategoryToPostCategoryDTO (PostCategory postCategory);

    List<PostCategoryDTO> postCategoryListToPostCategoryDTOList (List<PostCategory> postCategories);

}
