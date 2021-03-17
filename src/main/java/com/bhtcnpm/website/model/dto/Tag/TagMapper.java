package com.bhtcnpm.website.model.dto.Tag;

import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Mapper
public abstract class TagMapper {

    public static final TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    protected TagRepository tagRepository;

    public abstract List<TagDTO> tagListToTagDTOList (List<Tag> tags);

    public abstract Set<Tag> tagDTOListToTagList (Set<TagDTO> tagDTOs);

    public abstract TagDTO tagToTagDTO (Tag tag);

    public Tag tagDTOToTag (TagDTO tagDTO) {
        Tag resultTag;

        if (tagDTO == null) {
            return null;
        }

        if (tagDTO.getId() == null) {
            resultTag = Tag.builder()
                    .id(null)
                    .content(tagDTO.getContent())
                    .build();
        } else {
            resultTag = tagRepository.getOne(tagDTO.getId());
        }

        return resultTag;
    }

    @Autowired
    public void setTagRepository (TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
}
