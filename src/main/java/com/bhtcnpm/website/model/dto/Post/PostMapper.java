package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.entity.PostEntities.Post;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public abstract class PostMapper {

    public static final PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

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
}
