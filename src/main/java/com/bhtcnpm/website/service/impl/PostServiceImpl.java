package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostLike;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostLikeId;
import com.bhtcnpm.website.repository.PostRepository;
import com.bhtcnpm.website.repository.UserPostLikeRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.service.PostService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final UserPostLikeRepository userPostLikeRepository;

    private final UserWebsiteRepository userWebsiteRepository;

    private final PostMapper postMapper;

    private static final int PAGE_SIZE = 10;

    @Override
    public List<PostStatisticDTO> getPostStatistic(List<Long> postIDs, Long userID) {
        List<PostStatisticDTO> postStatisticDTOS = postRepository.getPostStatisticDTOs(postIDs, userID);

        return postStatisticDTOS;
    }

    @Override
    public PostSummaryListDTO getPostSummary(Predicate predicate, Integer paginator) {
        Sort sort;
        Pageable pageable = PageRequest.of(paginator, PAGE_SIZE);
        Page<Post> queryResults = postRepository.findAll(predicate, pageable);

        PostSummaryListDTO postSummaryListDTO = postMapper.postPageToPostSummaryListDTO(queryResults);

        return postSummaryListDTO;
    }

    @Override
    public PostDetailsDTO getPostDetails(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return postMapper.postToPostDetailsDTO(post.get());
        }
        return null;
    }

    @Override
    public Boolean approvePost(Long postID, Long userID) {
        int rowAffected = postRepository.approvePost(postID, userID);
        return rowAffected == 1;
    }

    @Override
    public Boolean deletePostApproval(Long postID) {
        int rowAffected = postRepository.deletePostApproval(postID);
        return rowAffected == 1;
    }

    @Override
    public Boolean createUserPostLike(Long postID, Long userID) {
        UserPostLikeId id = new UserPostLikeId();
        id.setPost(postRepository.getOne(postID));
        id.setUser(userWebsiteRepository.getOne(userID));
        UserPostLike userPostLike = new UserPostLike();
        userPostLike.setUserPostLikeId(id);

        userPostLikeRepository.save(userPostLike);

        return true;
    }

    @Override
    public Boolean deleteUserPostLike(Long postID, Long userID) {
        UserPostLikeId id = new UserPostLikeId();
        id.setPost(postRepository.getOne(postID));
        id.setUser(userWebsiteRepository.getOne(userID));

        userPostLikeRepository.deleteById(id);

        return true;
    }

    @Override
    public PostDetailsDTO createPost(PostRequestDTO postRequestDTO, Long userID) {
        Post post = postMapper.postRequestDTOToPost(postRequestDTO, userID, null);

        return postMapper.postToPostDetailsDTO(postRepository.save(post));
    }

    @Override
    public PostDetailsDTO editPost(PostRequestDTO postRequestDTO, Long postID, Long userID) {
        Optional<Post> optionalPost = postRepository.findById(postID);
        if (!optionalPost.isPresent()) {
            return null;
        }

        Post post = optionalPost.get();

        post = postMapper.postRequestDTOToPost(postRequestDTO, userID, post);

        return postMapper.postToPostDetailsDTO(post);
    }

    @Override
    public Boolean rejectPost(Long postID, Long userID) {
        return null;
    }
}
