package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.model.entity.UserWebsite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.springframework.http.HttpHeaders;

@Mapper(uses = UserDetailsMapper.class)
public interface UserAuthenticatedMapper {
    @Mapping(source = "userWebsite", target = "userDetailsDTO")
    @Mapping(source = "headers", target = "headers")
    UserAuthenticatedDTO userWebsiteToUserAuthenticatedDTO (UserWebsite userWebsite, HttpHeaders headers);
}
