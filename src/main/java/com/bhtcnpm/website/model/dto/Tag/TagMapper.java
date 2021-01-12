package com.bhtcnpm.website.model.dto.Tag;

import com.bhtcnpm.website.model.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    List<TagDTO> tagListToTagDTOList (List<Tag> tags);

    TagDTO tagToTagDTO (Tag tag);
}
