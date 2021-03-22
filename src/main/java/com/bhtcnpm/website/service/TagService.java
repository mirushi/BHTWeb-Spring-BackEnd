package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;

import java.util.List;

public interface TagService {
    List<TagDTO> getTagQuickSearch (String content);
    TagDTO getTagByID (Long id);
}
