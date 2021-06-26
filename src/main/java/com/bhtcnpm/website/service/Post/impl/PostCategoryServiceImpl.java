package com.bhtcnpm.website.service.Post.impl;

import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryDTO;
import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryMapper;
import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryRequestDTO;
import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import com.bhtcnpm.website.repository.Post.PostCategoryRepository;
import com.bhtcnpm.website.repository.Post.PostRepository;
import com.bhtcnpm.website.service.Post.PostCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCategoryServiceImpl implements PostCategoryService {

    private final PostCategoryRepository postCategoryRepository;

    private final PostCategoryMapper postCategoryMapper;

    private final PostRepository postRepository;

    @Override
    public List<PostCategoryDTO> getAllPostCategories() {
        List<PostCategory> queryResult = postCategoryRepository.findAll();

        List<PostCategoryDTO> result = postCategoryMapper.postCategoryListToPostCategoryDTOList(queryResult);

        return result;
    }

    @Override
    public PostCategoryDTO postPostCategory(PostCategoryRequestDTO postCategoryRequestDTO) {

        PostCategory postCategory = postCategoryMapper.postCategoryDTOToPostCategory(postCategoryRequestDTO, null);

        postCategory = postCategoryRepository.save(postCategory);

        return postCategoryMapper.postCategoryToPostCategoryDTO(postCategory);
    }

    @Override
    public PostCategoryDTO putPostCategory(PostCategoryRequestDTO postCategoryRequestDTO, Long postCategoryID) {

        Optional<PostCategory> optionalPostCategory = postCategoryRepository.findById(postCategoryID);

        if (!optionalPostCategory.isPresent()) {
            return null;
        }

        PostCategory postCategory = postCategoryMapper.postCategoryDTOToPostCategory(postCategoryRequestDTO, optionalPostCategory.get());

        postCategory = postCategoryRepository.save(postCategory);

        return postCategoryMapper.postCategoryToPostCategoryDTO(postCategory);
    }

    @Override
    public void deletePostCategory(Long postCategoryID) {
        //Đầu tiên phải kiểm tra xem category có đang tồn tại bài post không.
        //Nếu category đang tồn tại bài post thì không cho phép người dùng xoá.

        boolean existAnyPost = postRepository.existsByCategoryId(postCategoryID);

        if (existAnyPost) {
            throw new IllegalArgumentException("Post category contains Post(s), cannot be deleted.");
        }

        postCategoryRepository.deleteById(postCategoryID);
    }
}
