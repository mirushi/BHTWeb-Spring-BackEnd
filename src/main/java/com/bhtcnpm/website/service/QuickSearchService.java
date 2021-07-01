package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.QuickSearch.QuickSearchResultDTO;

public interface QuickSearchService {

    QuickSearchResultDTO quickSearch (String searchTerm);

}
