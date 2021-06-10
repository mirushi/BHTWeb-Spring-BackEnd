package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.constant.business.Post.HighlightPostBusinessConstant;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class HighlightPostDTO {
    private PostSummaryDTO postSummaryDTO;

    private Integer rank;
}
