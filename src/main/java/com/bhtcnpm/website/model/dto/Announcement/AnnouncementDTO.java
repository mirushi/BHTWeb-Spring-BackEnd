package com.bhtcnpm.website.model.dto.Announcement;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

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
