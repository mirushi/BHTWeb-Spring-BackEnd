package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryDTO;

import java.util.List;

public interface PostCategoryService {

    List<PostCategoryDTO> getAllPostCategories ();

    PostCategoryDTO postPostCategory (PostCategoryDTO postCategoryDTO);

    PostCategoryDTO putPostCategory (PostCategoryDTO postCategoryDTO, Long postCategoryID);
}
