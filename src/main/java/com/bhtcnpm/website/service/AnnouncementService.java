package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Announcement.AnnouncementDTO;
import com.bhtcnpm.website.model.dto.Announcement.AnnouncementListDTO;

import javax.validation.constraints.Min;

public interface AnnouncementService {

    AnnouncementListDTO getAllAnnouncements (Integer paginator);

    AnnouncementListDTO getAllActiveAnnouncements(Integer paginator);

    AnnouncementDTO saveAnnouncementsByDTO (AnnouncementDTO announcementDTO);

    AnnouncementDTO createAnnouncementsByDTO (AnnouncementDTO announcementDTO);

    void deleteAnnouncementByID (Long id);

}
