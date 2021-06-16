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

    @Mapping(source = "author.id", target = "authorID")
    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "category.id", target = "categoryID")
    @Mapping(source = "subject.name", target = "subject")
    @Mapping(source = "subject.id", target = "subjectID")
    @Mapping(source = "docFileUpload.downloadCount", target = "downloadCount")
    @Mapping(source = "publishDtm", target = "publishDtm")
    @Mapping(source = "viewCount", target = "viewCount")
    DocDetailsDTO docToDocDetailsDTO (Doc doc);

    List<DocDetailsDTO> docListToDocDetailsDTOList (List<Doc> docs);
}