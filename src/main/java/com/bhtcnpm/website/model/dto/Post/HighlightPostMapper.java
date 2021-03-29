package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.entity.PostEntities.HighlightPost;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.repository.PostRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = PostMapper.class)
public abstract class HighlightPostMapper {
    public static final HighlightPostMapper INSTANCE = Mappers.getMapper(HighlightPostMapper.class);

    protected PostRepository postRepository;
    protected UserWebsiteRepository userWebsiteRepository;

    @Mapping(target = "postSummaryDTO", source = "highlightPost.post")
    public abstract HighlightPostDTO highlightPostToHighlightPostDTO (HighlightPost highlightPost);

    public abstract List<HighlightPostDTO> highlightPostListToHighlightPostDTOList (List<HighlightPost> highlightPostList);

    public HighlightPost highlightPostRequestToHighlightPost (HighlightPostUpdateDTO dto, Long userID) {
        Post postProxy = postRepository.getOne(dto.getId());
        UserWebsite user = userWebsiteRepository.getOne(userID);

        return HighlightPost.builder()
                .post(postProxy)
                .highlightedBy(user)
                .rank(dto.getRank())
                .build();
    }

    public abstract List<HighlightPost> highlightPostUpdateRequestListToHighlightPostList (List<HighlightPostUpdateDTO> requestDTOList);

    @Autowired
    public void setPostRepository (PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Autowired
    public void setUserWebsiteRepository (UserWebsiteRepository userWebsiteRepository) {
        this.userWebsiteRepository = userWebsiteRepository;
    }

}
