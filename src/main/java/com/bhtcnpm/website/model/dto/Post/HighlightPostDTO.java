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
    @Valid
    @NotNull
    private PostSummaryDTO postSummaryDTO;

    @NotNull
    @Min(value = HighlightPostBusinessConstant.RANK_MIN)
    @Max(value = HighlightPostBusinessConstant.RANK_MAX)
    private Integer rank;
}
