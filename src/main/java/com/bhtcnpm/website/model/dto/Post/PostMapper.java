package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.dto.Tag.TagMapper;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.repository.PostCategoryRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.jsoup.Jsoup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Mapper
public abstract class PostMapper {

    public static final PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    protected UserWebsiteRepository userWebsiteRepository;
    protected PostCategoryRepository postCategoryRepository;
    protected TagMapper tagMapper;

    @Mapping(target = "authorID", source = "post.author.id")
    @Mapping(target = "authorName", source = "post.author.name")
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    public abstract PostSummaryDTO postToPostSummaryDTO (Post post);

    public abstract List<PostSummaryDTO> postToPostSummaryDTOs (List<Post> posts);

    @Mapping(target = "authorID", source = "post.author.id")
    @Mapping(target = "authorName", source = "post.author.name")
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    public abstract PostDetailsDTO postToPostDetailsDTO (Post post);

    public PostSummaryListDTO postPageToPostSummaryListDTO (Page<Post> postPage) {
        return new PostSummaryListDTO(postToPostSummaryDTOs(postPage.getContent()), postPage.getTotalPages());
    }

    public abstract List<PostSummaryDTO> postPageToPostSummaryDTOList (Page<Post> postPage);

    public Post postRequestDTOToPost (PostRequestDTO postRequestDTO, Long userID, Post entity) {
        Post post = Objects.requireNonNullElseGet(entity, Post::new);

        if (postRequestDTO == null || userID == null) {
            return entity;
        }

        //If current entity existed, we don't set new author. We just update last updated.
        if (entity != null) {
            post.setAuthor(userWebsiteRepository.getOne(userID));
            post.setLastUpdatedBy(userWebsiteRepository.getOne(userID));
            post.setLastUpdatedDtm(LocalDateTime.now());
            post.setPostState(PostStateType.PENDING_APPROVAL);
        }

        post.setCategory(postCategoryRepository.getOne(postRequestDTO.getCategoryID()));
        post.setContent(postRequestDTO.getContent());
        post.setImageURL(postRequestDTO.getImageURL());

        //Calculate reading time.
        post.setReadingTime(calculateReadTime(postRequestDTO.getContent()));

        post.setPublishDtm(LocalDateTime.now());
        post.setTags(tagMapper.tagDTOListToTagList(postRequestDTO.getTags()));
        post.setTitle(postRequestDTO.getTitle());
        post.setSummary(postRequestDTO.getSummary());

        return post;
    }

    protected String stripHTMLTag (String htmlContent) {
        return Jsoup.parse(htmlContent).text();
    }

    protected Integer calculateReadTime (String htmlContent) {

        String realText = stripHTMLTag(htmlContent);

        Integer readSpeedPerSec = 4;
        String trim = realText.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length / readSpeedPerSec;
    }

    @Autowired
    public void setUserWebsiteRepository (UserWebsiteRepository userWebsiteRepository) {
        this.userWebsiteRepository = userWebsiteRepository;
    }

    @Autowired
    public void setPostCategoryRepository (PostCategoryRepository postCategoryRepository) {
        this.postCategoryRepository = postCategoryRepository;
    }

    @Autowired
    public void setTagMapper (TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

}
