package com.bhtcnpm.website.model.dto.Activity;

import com.bhtcnpm.website.model.entity.enumeration.ActivityType.ActivityType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
public class ActivityDTO {

    private Long id;

    @NotNull
    private Long actorActiveID;

    @NotNull
    private String actorActiveName;

    private Long actorPassiveID;
    private String actorPassiveName;

    @PastOrPresent
    @NotNull
    private LocalDateTime activityDtm;

    @NotNull
    private Long idItem;
    @NotNull
    private ActivityType type;
    @NotNull
    private short version;
}
