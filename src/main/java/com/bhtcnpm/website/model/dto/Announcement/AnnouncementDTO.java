package com.bhtcnpm.website.model.dto.Announcement;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AnnouncementDTO {

    private Long id;

    @Size(min = 3, max = 255)
    @NotBlank
    private String content;

    @NotNull
    private Boolean activatedStatus;

    @NotNull
    private short version;
}
