package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.constant.domain.Exercise.ExerciseBusinessState;
import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseQuickSearchResult;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTOList;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSuggestionDTO;
import com.bhtcnpm.website.model.dto.Exercise.filter.ExerciseSearchFilterRequestDTO;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseSearchMapper;
import com.bhtcnpm.website.model.dto.Exercise.sort.ExerciseSearchSortRequestDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.Doc.DocRepository;
import com.bhtcnpm.website.repository.Exercise.custom.ExerciseSearchRepository;
import com.bhtcnpm.website.repository.Post.PostRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseSearchService;
import com.bhtcnpm.website.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseSearchServiceImpl implements ExerciseSearchService {

    private final ExerciseSearchRepository exerciseSearchRepository;
    private final PostRepository postRepository;
    private final DocRepository docRepository;
    private final ExerciseSearchMapper exerciseSearchMapper;
    private static final int PAGE_SIZE = 10;
    private static final int RELATED_SIZE = 3;

    @Override
    public ExerciseSearchResultDTOList searchExercise(ExerciseSearchFilterRequestDTO filterRequestDTO, ExerciseSearchSortRequestDTO sortRequestDTO, Integer page, Authentication authentication) {
        Page<Exercise> searchResult = exerciseSearchRepository.searchPublicExercise(filterRequestDTO, sortRequestDTO, page, PAGE_SIZE, authentication);

        return exerciseSearchMapper.exercisePageToExerciseSearchResultDTOList(searchResult);
    }

    @Override
    public List<ExerciseQuickSearchResult> quickSearch(Pageable pageable, String searchTerm) {
        ExerciseSearchFilterRequestDTO dto = new ExerciseSearchFilterRequestDTO();
        dto.setSearchTerm(searchTerm);
        Page<Exercise> searchResult = exerciseSearchRepository.searchPublicExercise(dto, null, pageable.getPageNumber(), pageable.getPageSize(), null);

        return exerciseSearchMapper.exerciseListToExerciseQuickSearchResultList(searchResult.getContent());
    }

    @Override
    public List<ExerciseSuggestionDTO> getRelatedExercises(Long postID, Long docID, Integer page, Authentication authentication) throws IOException {
        if (page == null) {
            page = 0;
        }

        ValidationUtils.assertExactlyOneParamIsNotNull(postID, docID);
        String title = null;
        String description = null;

        if (postID != null) {
            Optional<Post> object = postRepository.findById(postID);
            if (object.isEmpty()) {
                throw new IDNotFoundException();
            }
            Post post = object.get();
            title = post.getTitle();
            description = post.getSummary();
        } else if (docID != null) {
            Optional<Doc> object = docRepository.findById(docID);
            if (object.isEmpty()) {
                throw new IDNotFoundException();
            }
            Doc doc = object.get();
            title = doc.getTitle();
            description = doc.getDescription();
        }

        Page<Exercise> searchResult = exerciseSearchRepository.getRelatedExercise(title, description, page, RELATED_SIZE, ExerciseBusinessState.PUBLIC);

        return exerciseSearchMapper.exerciseListToExerciseSuggestionDTOList(searchResult.getContent());
    }
}
