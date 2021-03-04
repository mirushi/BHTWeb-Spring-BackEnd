package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.model.entity.UserWebsite;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserSummaryMapper {
    UserSummaryDTO userWebsiteToUserSummaryDTO (UserWebsite userWebsite);
}
