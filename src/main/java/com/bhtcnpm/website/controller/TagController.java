package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@Validated
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/quickQuery")
    @ResponseBody
    public ResponseEntity<List<TagDTO>> tagQuickQuery (@RequestParam String query) {
        List<TagDTO> tagDTOs = tagService.getTagQuickSearch(query);

        return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<TagDTO> getTagById (@RequestParam Long id) {
        TagDTO tag = tagService.getTagByID(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

}
