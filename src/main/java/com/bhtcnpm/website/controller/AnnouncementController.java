package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.Announcement.AnnouncementDTO;
import com.bhtcnpm.website.model.dto.Announcement.AnnouncementListDTO;
import com.bhtcnpm.website.model.entity.Announcement;
import com.bhtcnpm.website.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/announcements")
@Validated
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<AnnouncementListDTO> getAllAnnouncement (@RequestParam @Min(value = 0, message = "paginator must be greater than or equals to 0") Integer paginator) {

        if (paginator == null) {
            paginator = 0;
        }

        AnnouncementListDTO announcementList = announcementService.getAllAnnouncements(paginator);

        return new ResponseEntity<>(announcementList, HttpStatus.OK) ;
    }

    @GetMapping("/showcase")
    @ResponseBody
    public ResponseEntity<AnnouncementListDTO> getAllActiveAnnouncement (@RequestParam @Min(value = 0, message = "paginator must be greater than or equals to 0") Integer paginator) {

        if (paginator == null) {
            paginator = 0;
        }

        AnnouncementListDTO announcementList = announcementService.getAllActiveAnnouncements(paginator);

        return new ResponseEntity<>(announcementList, HttpStatus.OK) ;
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<AnnouncementDTO> putAnnouncement (@RequestBody @Valid AnnouncementDTO announcementDTO, BindingResult result) {
        if (announcementDTO == null) {
            return null;
        }

        if (result.hasErrors()) {
            throw new IllegalArgumentException();
        }

        AnnouncementDTO newAnnouncement = announcementService.saveAnnouncementsByDTO(announcementDTO);

        return new ResponseEntity<>(newAnnouncement, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<AnnouncementDTO> postAnnouncement (@RequestBody @Valid AnnouncementDTO announcementDTO, BindingResult result) {
        if (announcementDTO == null) {
            return null;
        }

        if (result.hasErrors()) {
            throw new IllegalArgumentException();
        }

        AnnouncementDTO newAnnouncement = announcementService.createAnnouncementsByDTO(announcementDTO);

        return new ResponseEntity<>(newAnnouncement, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAnnouncement (@PathVariable @Min(0) Long id) {
        announcementService.deleteAnnouncementByID(id);
    }
}
