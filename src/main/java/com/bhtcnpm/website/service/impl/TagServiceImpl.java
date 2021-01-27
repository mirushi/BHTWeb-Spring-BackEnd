package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.dto.Tag.TagMapper;
import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.repository.TagRepository;
import com.bhtcnpm.website.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    private static final int QUICK_SEARCH_RESULT_LIMIT = 3;

    @Override
    public List<TagDTO> getTagQuickSearch(String content) {
        Pageable pageable = PageRequest.of(0, QUICK_SEARCH_RESULT_LIMIT);
        List<Tag> finalQueryResult = tagRepository.findByContentEqualsOrContentContainingIgnoreCase(pageable, content, content);

        List<TagDTO> result = tagMapper.tagListToTagDTOList(finalQueryResult);

        return result;
    }
}
