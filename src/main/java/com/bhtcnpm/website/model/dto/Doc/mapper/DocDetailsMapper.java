package com.bhtcnpm.website.model.dto.Doc.mapper;

import com.bhtcnpm.website.model.dto.Doc.DocDetailsDTO;
import com.bhtcnpm.website.model.dto.Tag.TagMapper;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {TagMapper.class})
public interface DocDetailsMapper {

    DocDetailsMapper INSTANCE = Mappers.getMapper(DocDetailsMapper.class);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "subjectID", source = "subject.id")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(target = "publishDtm", source = "publishDtm")
    DocDetailsDTO docToDocDetailsDTO (Doc doc);

    List<DocDetailsDTO> docListToDocDetailsDTOList (List<Doc> docs);
}
