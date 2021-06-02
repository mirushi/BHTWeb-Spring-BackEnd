package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.constant.business.Post.HighlightPostBusinessConstant;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class HighlightPostUpdateDTO {
    @Min(0)
    private Long id;

    @NotNull
    @Min(value = HighlightPostBusinessConstant.RANK_MIN)
    @Max(value = HighlightPostBusinessConstant.RANK_MAX)
    private Integer rank;
}
