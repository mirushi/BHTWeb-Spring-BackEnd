package com.bhtcnpm.website.model.dto.Google;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CaptchaRequestDTO {
    private String secret;
    private String response;
    private String remoteip;
}
