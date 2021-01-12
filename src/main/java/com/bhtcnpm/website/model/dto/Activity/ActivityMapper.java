package com.bhtcnpm.website.model.dto.Activity;

import com.bhtcnpm.website.model.entity.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActivityMapper {

    ActivityMapper INSTANCE = Mappers.getMapper(ActivityMapper.class);

    @Mapping (source = "actorActive.id", target = "actorActiveID")
    @Mapping (source = "actorActive.name", target = "actorActiveName")
    @Mapping (source = "actorPassive.id", target = "actorPassiveID")
    @Mapping (source = "actorPassive.name", target = "actorPassiveName")
    ActivityDTO activityToActivityDTO (Activity activity);

}
