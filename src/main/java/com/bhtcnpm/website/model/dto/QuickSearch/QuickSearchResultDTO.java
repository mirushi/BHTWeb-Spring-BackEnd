package com.bhtcnpm.website.model.dto.QuickSearch;

import com.bhtcnpm.website.model.dto.Doc.DocQuickSearchResult;
import com.bhtcnpm.website.model.dto.Post.PostQuickSearchResult;
import com.bhtcnpm.website.model.dto.Tag.TagQuickSearchResult;
import lombok.Data;

import java.util.List;

@Data
public class QuickSearchResultDTO {
    private List<PostQuickSearchResult> postQuickSearchResults;
    private List<DocQuickSearchResult> docQuickSearchResults;
    private List<TagQuickSearchResult> tagQuickSearchResults;
}
