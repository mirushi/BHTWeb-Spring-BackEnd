package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.dto.Tag.TagMapper;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.repository.PostCategoryRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper
public abstract class PostMapper {

    public static final PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    protected UserWebsiteRepository userWebsiteRepository;
    protected PostCategoryRepository postCategoryRepository;
    protected TagMapper tagMapper;

    @Mapping(target = "authorID", source = "post.author.id")
    @Mapping(target = "authorName", source = "post.author.name", qualifiedBy = {})
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "authorAvatarURL", source = "post.author.avatarURL")
    public abstract PostSummaryDTO postToPostSummaryDTO (Post post);

    @Mapping(target = "authorAvatarURL", source = "post.author.avatarURL")
    @Mapping(target = "authorID", source = "post.author.id")
    @Mapping(target = "authorName", source = "post.author.name", qualifiedBy = {})
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    public abstract PostSummaryWithStateDTO postToPostSummaryWithStateDTO (Post post);

    public abstract List<PostSummaryWithStateDTO> postListToPostSummaryWithStateDTOList (List<Post> postList);

    public abstract List<PostSummaryDTO> postListToPostSummaryDTOs(List<Post> posts);

    @Mapping(target = "authorID", source = "post.author.id")
    @Mapping(target = "authorName", source = "post.author.name")
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "authorAvatarURL", source = "author.avatarURL")
    public abstract PostDetailsDTO postToPostDetailsDTO (Post post);

    public PostSummaryListDTO postPageToPostSummaryListDTO (Page<Post> postPage) {
        return new PostSummaryListDTO(postListToPostSummaryDTOs(postPage.getContent()), postPage.getTotalPages(), postPage.getTotalElements());
    }

    public PostSummaryListDTO postSummaryPageToPostSummaryListDTO (Page<PostSummaryDTO> postSummaryDTOs) {
        return new PostSummaryListDTO(postSummaryDTOs.get().collect(Collectors.toList()), postSummaryDTOs.getTotalPages(), postSummaryDTOs.getTotalElements());
    }

    public abstract List<PostSummaryDTO> postPageToPostSummaryDTOList (Page<Post> postPage);

    public Post postRequestDTOToPost (PostRequestDTO postRequestDTO, UUID userID, Post entity) {
        Post post = Objects.requireNonNullElseGet(entity, Post::new);
        String contentCleansed = stripDangerousHTMLTag(postRequestDTO.getContent());
        String contentPlainText = stripAllHTMLTag(postRequestDTO.getContent());

        if (userID == null) {
            return entity;
        }

        //If current entity existed, we don't set new author. We just update last updated.
        if (entity != null) {
            post.setLastUpdatedBy(userWebsiteRepository.getOne(userID));
            post.setLastUpdatedDtm(LocalDateTime.now());
        } else {
            post.setAuthor(userWebsiteRepository.getOne(userID));
            post.setPostState(PostStateType.PENDING_APPROVAL);
        }

        post.setCategory(postCategoryRepository.getOne(postRequestDTO.getCategoryID()));
        post.setContent(contentCleansed);
        post.setContentPlainText(contentPlainText);
        post.setImageURL(postRequestDTO.getImageURL());

        //Calculate reading time.
        post.setReadingTime(calculateReadTime(postRequestDTO.getContent()));

        post.setPublishDtm(LocalDateTime.now());
        post.setTags(tagMapper.tagDTOListToTagList(postRequestDTO.getTags()));
        post.setTitle(postRequestDTO.getTitle());
        post.setSummary(postRequestDTO.getSummary());

        return post;
    }

    @Named("stripAllHTMLTag")
    protected String stripAllHTMLTag (String htmlContent) {
        return Jsoup.parse(htmlContent, "UTF-8").text();
    }

    @Named("stripDangerousHTMLTag")
    protected String stripDangerousHTMLTag(String htmlContent) {
        Whitelist relaxedWhiteList = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(relaxedWhiteList);

        Document sanitizedDocument = cleaner.clean(Jsoup.parse(htmlContent, "UTF-8"));

        return sanitizedDocument.body().html();
    }

    protected Integer calculateReadTime (String htmlContent) {

        String realText = stripAllHTMLTag(htmlContent);

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
