package com.bhtcnpm.website.model.dto.PostCategory;

import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

@Mapper
public abstract class PostCategoryMapper {

    public static final PostCategoryMapper INSTANCE = Mappers.getMapper(PostCategoryMapper.class);

    public abstract PostCategoryDTO postCategoryToPostCategoryDTO (PostCategory postCategory);

    public abstract List<PostCategoryDTO> postCategoryListToPostCategoryDTOList (List<PostCategory> postCategories);

    public PostCategory postCategoryDTOToPostCategory (PostCategoryRequestDTO postCategoryRequestDTO, PostCategory entity) {
        PostCategory category = Objects.requireNonNullElseGet(entity, PostCategory::new);

        if (postCategoryRequestDTO == null) {
            return entity;
        }

        category.setName(postCategoryRequestDTO.getName());

        return category;
    }

}
