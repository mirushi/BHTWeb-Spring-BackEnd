package com.bhtcnpm.website.model.dto.Announcement;

import com.bhtcnpm.website.model.entity.Announcement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(imports = LocalDateTime.class)
public interface AnnouncementMapper {

    AnnouncementMapper INSTANCE = Mappers.getMapper(AnnouncementMapper.class);

    AnnouncementDTO announcementToAnnouncementDTO (Announcement announcement);

    @Mapping(target = "lastUpdated", expression = "java(LocalDateTime.now())")
    Announcement announcementDTOToAnnouncement (AnnouncementDTO announcementDTO);
}
