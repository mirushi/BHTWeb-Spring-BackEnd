package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.Data;
import org.springframework.http.HttpHeaders;

@Data
public class UserAuthenticatedDTO {
    private HttpHeaders headers;
    private UserDetailsDTO userDetailsDTO;
}
