package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.QuickSearch.QuickSearchResultDTO;

import java.util.List;

public interface QuickSearchService {

    QuickSearchResultDTO quickSearch (String searchTerm);

}
