package com.bhtcnpm.website.model.dto.Doc.mapper;

import com.bhtcnpm.website.model.dto.Doc.DocSuggestionDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface DocSuggestionMapper {

    DocSuggestionDTO docToDocSuggestion (Doc doc);

    default List<DocSuggestionDTO> docPageToDocSuggestionDTOList (Page<Doc> docs) {
        return docs.get().map(this::docToDocSuggestion).collect(Collectors.toList());
    }
}