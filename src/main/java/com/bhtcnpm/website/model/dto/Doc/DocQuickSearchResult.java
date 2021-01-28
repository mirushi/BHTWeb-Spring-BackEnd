package com.bhtcnpm.website.model.dto.Doc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocQuickSearchResult {
    private Long id;
    private String imageURL;
    private String title;
}
