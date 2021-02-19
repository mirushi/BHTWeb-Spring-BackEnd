package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.model.entity.UserWebsite;
import org.mapstruct.Mapper;

@Mapper
public abstract class UserDetailsMapper {
    public abstract UserDetailsDTO userWebsiteToUserDetailsDTO (UserWebsite userWebsite);
}
