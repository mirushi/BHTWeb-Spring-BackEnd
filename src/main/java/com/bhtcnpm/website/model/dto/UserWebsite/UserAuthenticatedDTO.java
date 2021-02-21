package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;

@Data
@AllArgsConstructor
public class UserAuthenticatedDTO {
    private HttpHeaders headers;
    private UserDetailsDTO userDetailsDTO;
}
