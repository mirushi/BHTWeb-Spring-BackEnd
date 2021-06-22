package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionUserOwnDTO;
import com.bhtcnpm.website.service.UserDocReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
public class UserDocReactionController {

    private final UserDocReactionService userDocReactionService;

    @GetMapping("/documents/reactions")
    @ResponseBody
    public ResponseEntity<List<UserDocReactionStatsDTO>> getDocStats (@RequestParam (value = "docIDs") List<Long> docIDs) {
        List<UserDocReactionStatsDTO> stats = userDocReactionService.getDocsStats(docIDs);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @GetMapping("/documents/reactions/userOwn")
    @ResponseBody
    public ResponseEntity<List<UserDocReactionUserOwnDTO>> getUserOwnReaction (@RequestParam(value = "docIDs") List<Long> docIDs,
                                                                               Authentication authentication) {
        List<UserDocReactionUserOwnDTO> userDocReactions = userDocReactionService.getUserReactionForDocs(docIDs, authentication);
        return new ResponseEntity<>(userDocReactions, HttpStatus.OK);
    }

    @PutMapping("/documents/{id}/reactions")
    @ResponseBody
    public ResponseEntity<UserDocReactionDTO> putUserReactionForDoc (@RequestBody UserDocReactionUserOwnDTO userDocReactionUserOwnDTO,
                                                                     @PathVariable("id") Long docID,
                                                                     Authentication authentication) {
        UserDocReactionDTO responseDTO = userDocReactionService.putUserReactionForDoc(docID, userDocReactionUserOwnDTO, authentication);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
