package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.model.entity.UserWebsite;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserSummaryMapper {
    UserSummaryDTO userWebsiteToUserSummaryDTO (UserWebsite userWebsite);

    List<UserSummaryDTO> userWebsiteListToUserSummaryDTOList (List<UserWebsite> userWebsiteList);
}
