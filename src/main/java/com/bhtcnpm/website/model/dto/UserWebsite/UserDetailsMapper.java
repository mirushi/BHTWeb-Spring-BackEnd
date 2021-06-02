package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.model.entity.UserWebsite;
import org.mapstruct.Mapper;

@Mapper
public interface UserDetailsMapper {
    UserDetailsDTO userWebsiteToUserDetailsDTO (UserWebsite userWebsite);
}
