package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.entity.Tag;

import java.util.List;

public interface TagRepositoryCustom {
    List<TagDTO> getRelatedTags (Tag entity, int limit);
}
