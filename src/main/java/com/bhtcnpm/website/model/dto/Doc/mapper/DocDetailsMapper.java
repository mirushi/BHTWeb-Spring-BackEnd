package com.bhtcnpm.website.model.dto.Doc.mapper;

import com.bhtcnpm.website.model.dto.Doc.DocDetailsDTO;
import com.bhtcnpm.website.model.dto.Doc.DocDetailsWithStateDTO;
import com.bhtcnpm.website.model.dto.Doc.DocDetailsWithStateListDTO;
import com.bhtcnpm.website.model.dto.Tag.TagMapper;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(uses = {TagMapper.class})
public abstract class DocDetailsMapper {

    public static DocDetailsMapper INSTANCE = Mappers.getMapper(DocDetailsMapper.class);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "subjectID", source = "subject.id")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(target = "publishDtm", source = "publishDtm")
    @Mapping(target = "docFileUploadDTOs", source = "docFileUploads")
    public abstract DocDetailsDTO docToDocDetailsDTO (Doc doc);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "subjectID", source = "subject.id")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(target = "publishDtm", source = "publishDtm")
    @Mapping(target = "docFileUploadDTOs", source = "docFileUploads")
    public abstract DocDetailsWithStateDTO docToDocDetailsWithStateDTO (Doc doc);

    public abstract List<DocDetailsWithStateDTO> docListToDocDetailsWithStateDTOList (List<Doc> docList);

    public DocDetailsWithStateListDTO docPageToDocDetailsWithStateListDTO (Page<Doc> docPage) {
        List<DocDetailsWithStateDTO> docDetailsWithStateDTO = this.docListToDocDetailsWithStateDTOList(docPage.getContent());
        DocDetailsWithStateListDTO docDetailsWithStateListDTO = new DocDetailsWithStateListDTO();

        docDetailsWithStateListDTO.setDocDetailsWithStateDTOs(docDetailsWithStateDTO);
        docDetailsWithStateListDTO.setTotalElements(docPage.getTotalElements());
        docDetailsWithStateListDTO.setTotalPages(docPage.getTotalPages());

        return docDetailsWithStateListDTO;
    }

    public abstract List<DocDetailsDTO> docListToDocDetailsDTOList (List<Doc> docs);
}
