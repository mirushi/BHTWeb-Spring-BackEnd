package com.bhtcnpm.website.model.dto.Doc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocStatisticDTO {
    private List<DocReactionStatisticDTO> docReactionStatisticDTOs;
    private List<DocUserOwnReactionStatisticDTO> docUserOwnReactionStatisticDTOs;
    private List<DocCommentStatisticDTO> docCommentStatisticDTOs;
}
