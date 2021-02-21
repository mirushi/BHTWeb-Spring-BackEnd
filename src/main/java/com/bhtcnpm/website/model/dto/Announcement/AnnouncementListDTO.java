package com.bhtcnpm.website.model.dto.Announcement;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnnouncementListDTO {
    private List<AnnouncementDTO> announcements;
    private Integer totalPages;
    private Long totalElements;
}
