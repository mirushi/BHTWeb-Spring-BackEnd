package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.entity.PostEntities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostSuggestionMapper {

    PostSuggestionMapper INSTANCE = Mappers.getMapper(PostSuggestionMapper.class);

    PostSuggestionDTO postToPostSuggestionDTO (Post post);

    List<PostSuggestionDTO> postListToPostSuggestionListDTO (List<Post> postList);

}
