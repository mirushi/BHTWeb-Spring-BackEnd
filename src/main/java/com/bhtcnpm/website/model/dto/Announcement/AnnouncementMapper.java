package com.bhtcnpm.website.model.dto.Announcement;

import com.bhtcnpm.website.model.entity.Announcement;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnnouncementMapper {

    AnnouncementMapper INSTANCE = Mappers.getMapper(AnnouncementMapper.class);

    AnnouncementDTO announcementToAnnouncementDTO (Announcement announcement);

    Announcement announcementDTOToAnnouncement (AnnouncementDTO announcementDTO);
}
