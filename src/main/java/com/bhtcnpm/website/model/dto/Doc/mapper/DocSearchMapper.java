package com.bhtcnpm.website.model.dto.Doc.mapper;

import com.bhtcnpm.website.model.dto.Doc.DocQuickSearchResult;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DocSearchMapper {
    DocQuickSearchResult docToDocQuickSearchResult (Doc doc);
    List<DocQuickSearchResult> docListToDocQuickSearchResultList (List<Doc> docList);
}
