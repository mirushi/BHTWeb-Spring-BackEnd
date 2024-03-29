package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.Doc.DocQuickSearchResult;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseQuickSearchResult;
import com.bhtcnpm.website.model.dto.Post.PostQuickSearchResult;
import com.bhtcnpm.website.model.dto.QuickSearch.QuickSearchResultDTO;
import com.bhtcnpm.website.model.dto.QuickSearch.QuickSearchResultMapper;
import com.bhtcnpm.website.model.dto.Tag.TagQuickSearchResult;
import com.bhtcnpm.website.repository.Doc.DocRepository;
import com.bhtcnpm.website.repository.Post.PostRepository;
import com.bhtcnpm.website.repository.TagRepository;
import com.bhtcnpm.website.service.Doc.DocService;
import com.bhtcnpm.website.service.Exercise.ExerciseSearchService;
import com.bhtcnpm.website.service.QuickSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuickSearchServiceImpl implements QuickSearchService {

    private final PostRepository postRepository;
    private final DocService docService;
    private final TagRepository tagRepository;
    private final ExerciseSearchService exerciseSearchService;

    private final QuickSearchResultMapper quickSearchResultMapper;

    private static final int POST_RESULT_LIMIT = 2;
    private static final int DOC_RESULT_LIMIT = 2;
    private static final int EXERCISE_RESULT_LIMIT = 2;
    private static final int TAG_RESULT_LIMIT = 2;

    @Override
    public QuickSearchResultDTO quickSearch(String searchTerm) {
        Pageable pageablePost = PageRequest.of(0, POST_RESULT_LIMIT);
        Pageable pageableDoc = PageRequest.of(0, DOC_RESULT_LIMIT);
        Pageable pageableTag = PageRequest.of(0, TAG_RESULT_LIMIT);
        Pageable pageableExercise = PageRequest.of(0, EXERCISE_RESULT_LIMIT);

        List<PostQuickSearchResult> postQuickSearchResults = postRepository.quickSearch(0, POST_RESULT_LIMIT ,searchTerm);
        List<DocQuickSearchResult> docQuickSearchResults = docService.quickSearch(pageableDoc, searchTerm);
        List<TagQuickSearchResult> tagQuickSearchResults = tagRepository.quickSearch(pageableTag, searchTerm, searchTerm);
        List<ExerciseQuickSearchResult> exerciseQuickSearchResults = exerciseSearchService.quickSearch(pageableExercise, searchTerm);

        QuickSearchResultDTO quickSearchResultDTO = quickSearchResultMapper.getQuickSearchResultDTO(
                postQuickSearchResults, docQuickSearchResults, tagQuickSearchResults,exerciseQuickSearchResults
        );

        return quickSearchResultDTO;
    }
}
