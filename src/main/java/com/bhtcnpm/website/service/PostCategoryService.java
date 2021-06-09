package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryDTO;
import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryRequestDTO;
import com.bhtcnpm.website.model.validator.dto.PostCategory.PostCategoryID;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import java.util.List;

public interface PostCategoryService {
    @PreAuthorize(value = "permitAll()")
    List<PostCategoryDTO> getAllPostCategories ();

    @PreAuthorize(value = "hasRole(" +
            "T(com.bhtcnpm.website.constant.security.permission.PostCategoryPermissionConstant).POSTCATEGORY_ALL_ALL_CREATE)")
    PostCategoryDTO postPostCategory (@Valid PostCategoryRequestDTO postCategoryDTO);

    @PreAuthorize(value = "hasRole(" +
            "T(com.bhtcnpm.website.constant.security.permission.PostCategoryPermissionConstant).POSTCATEGORY_ALL_ALL_UPDATE)")
    PostCategoryDTO putPostCategory (@Valid PostCategoryRequestDTO postCategoryDTO, @PostCategoryID Long postCategoryID);

    @PreAuthorize(value = "hasRole(" +
            "T(com.bhtcnpm.website.constant.security.permission.PostCategoryPermissionConstant).POSTCATEGORY_ALL_ALL_DELETE)")
    void deletePostCategory (@PostCategoryID Long postCategoryID);
}
