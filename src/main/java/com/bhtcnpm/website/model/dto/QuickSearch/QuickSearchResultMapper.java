package com.bhtcnpm.website.model.dto.QuickSearch;

import com.bhtcnpm.website.model.dto.Doc.DocQuickSearchResult;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseQuickSearchResult;
import com.bhtcnpm.website.model.dto.Post.PostQuickSearchResult;
import com.bhtcnpm.website.model.dto.Tag.TagQuickSearchResult;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class QuickSearchResultMapper {

    public QuickSearchResultDTO getQuickSearchResultDTO (List<PostQuickSearchResult> postQuickSearchResults,
                                                         List<DocQuickSearchResult> docQuickSearchResults, List<TagQuickSearchResult> tagQuickSearchResults,
                                                         List<ExerciseQuickSearchResult> exerciseQuickSearchResults) {
        QuickSearchResultDTO quickSearchResultDTO = new QuickSearchResultDTO();
        quickSearchResultDTO.setPostQuickSearchResults(postQuickSearchResults);
        quickSearchResultDTO.setDocQuickSearchResults(docQuickSearchResults);
        quickSearchResultDTO.setTagQuickSearchResults(tagQuickSearchResults);
        quickSearchResultDTO.setExerciseQuickSearchResults(exerciseQuickSearchResults);
        return quickSearchResultDTO;
    }
}
