package com.bhtcnpm.website.model.dto.Activity;

import com.bhtcnpm.website.model.entity.enumeration.ActivityType.ActivityType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ActivityDTO {

    private Long id;

    @NotNull
    private UUID actorActiveID;

    @NotNull
    private String actorActiveName;

    private UUID actorPassiveID;
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
