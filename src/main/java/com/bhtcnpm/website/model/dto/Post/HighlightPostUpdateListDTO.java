package com.bhtcnpm.website.model.dto.Post;

import lombok.Data;

import java.util.List;

@Data
public class HighlightPostUpdateListDTO {
    List<HighlightPostUpdateDTO> updateRequestDTOList;
}
