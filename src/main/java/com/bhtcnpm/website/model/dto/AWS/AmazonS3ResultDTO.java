package com.bhtcnpm.website.model.dto.AWS;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AmazonS3ResultDTO {
    String directURL;
}
