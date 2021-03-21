package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.entity.PostEntities.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PostSuggestionMapper {

    PostSuggestionDTO postToPostSuggestionDTO (Post post);

    List<PostSuggestionDTO> postListToPostSuggestionListDTO (List<Post> postList);

}
