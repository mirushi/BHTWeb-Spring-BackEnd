package com.bhtcnpm.website.model.dto.Post;

import com.bhtcnpm.website.constant.business.Post.HighlightPostBusinessConstant;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class HighlightPostUpdateListDTO {
    @Valid
    @Size(max = HighlightPostBusinessConstant.MAX_UPDATE_REQUEST_SIZE)
    List<HighlightPostUpdateDTO> updateRequestDTOList;
}
