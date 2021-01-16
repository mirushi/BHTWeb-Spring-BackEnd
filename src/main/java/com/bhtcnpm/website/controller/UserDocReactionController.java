package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionUserOwnDTO;
import com.bhtcnpm.website.model.entity.UserDocReaction;
import com.bhtcnpm.website.service.UserDocReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/documents/reactions")
@Validated
@RequiredArgsConstructor
public class UserDocReactionController {

    private final UserDocReactionService userDocReactionService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<UserDocReactionStatsDTO>> getDocStats (@RequestParam (value = "docIDs", required = true) List<Long> docIDs) {
        List<UserDocReactionStatsDTO> stats = userDocReactionService.getDocsStats(docIDs);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @GetMapping("userOwn")
    @ResponseBody
    public ResponseEntity<List<UserDocReactionUserOwnDTO>> getUserOwnReaction (@RequestParam(value = "docIDs", required = true) List<Long> docIDs) {
        //We'll use a hard-coded userID for now. We'll get userID from user login token later.
        List<UserDocReactionUserOwnDTO> userDocReactions = userDocReactionService.getUserReactionForDocs(1L, docIDs);
        return new ResponseEntity<>(userDocReactions, HttpStatus.OK);
    }
}
