package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.constant.business.Tag.TagBusinessConstant;
import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.dto.Tag.TagMapper;
import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.repository.TagRepository;
import com.bhtcnpm.website.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

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
        List<Tag> finalQueryResult = tagRepository.getSimilarTags(pageable, content ,content);

        List<TagDTO> result = tagMapper.tagListToTagDTOList(finalQueryResult);

        return result;
    }

    @Override
    public TagDTO getTagByID(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);

        if (tag.isEmpty()) {
            return null;
        }

        return tagMapper.tagToTagDTO(tag.get());
    }

    @Override
    public List<TagDTO> getRelatedTags(Long tagID) {
        Optional<Tag> optEntity = tagRepository.findById(tagID);
        if (optEntity.isEmpty()) {
            return null;
        }
        return tagRepository.getRelatedTags(optEntity.get(), TagBusinessConstant.RELATED_TAG_LIMIT);
    }
}
