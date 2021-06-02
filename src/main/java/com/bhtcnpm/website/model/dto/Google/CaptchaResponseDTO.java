package com.bhtcnpm.website.model.dto.Google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CaptchaResponseDTO {
    private Boolean success;

    @JsonProperty("challenge_ts")
    private LocalDateTime challengeTS;
    private String hostname;

    @JsonProperty("error-codes")
    private List<String> errorCodes;
}
