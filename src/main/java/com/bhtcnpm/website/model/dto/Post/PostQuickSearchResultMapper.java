package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.model.entity.PostEntities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostQuickSearchResultMapper {

    PostQuickSearchResultMapper INSTANCE = Mappers.getMapper(PostQuickSearchResultMapper.class);

    PostQuickSearchResult postToPostQuickSearchResult (Post post);

    List<PostQuickSearchResult> postListToPostQuickSearchResultList (List<Post> postList);
}
