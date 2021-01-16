package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TagMapperResolver {

    private final TagRepository tagRepository;

    @ObjectFactory
    public Tag resolve (TagDTO dto, @TargetType Class<Tag> type) {
        Tag result;
        if (dto != null){
            if (dto.getId() != null) {
                Optional<Tag> optionalTag = tagRepository.findById(dto.getId());

                if (!optionalTag.isPresent()) {
                    result = null;
                } else {
                    result = optionalTag.get();
                }

            } else if (dto.getContent() != null) {
                result = tagRepository.findByContent(dto.getContent());

                if (result == null) {
                    result = new Tag();
                    result.setContent(dto.getContent());
                }

            } else {
                result = null;
            }
        } else {
            result = null;
        }
        return result;
    }
}
