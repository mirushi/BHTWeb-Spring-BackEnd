package com.bhtcnpm.website.model.dto.Tag;

import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.repository.TagMapperResolver;
import com.bhtcnpm.website.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    List<TagDTO> tagListToTagDTOList (List<Tag> tags);

    TagDTO tagToTagDTO (Tag tag);

    @InheritInverseConfiguration(name = "tagToTagDTO")
    Tag tagDTOToTag (TagDTO tagDTO);
}
