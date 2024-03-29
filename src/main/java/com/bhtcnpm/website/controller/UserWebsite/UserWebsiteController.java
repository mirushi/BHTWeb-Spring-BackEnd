package com.bhtcnpm.website.controller.UserWebsite;

import com.bhtcnpm.website.model.dto.UserWebsite.*;
import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserWebsiteController {

    private final UserWebsiteService userWebsiteService;

    @GetMapping("/summary")
    @ResponseBody
    public ResponseEntity<UserSummaryWithStatisticDTO> getUserSummaryWithStatistic (Authentication authentication) {
        UserSummaryWithStatisticDTO userSummaryWithStatisticDTO = userWebsiteService.getUserSummaryWithStatistic(authentication);

        return new ResponseEntity<>(userSummaryWithStatisticDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/statistics")
    @ResponseBody
    public ResponseEntity<UserFullStatisticDTO> getUserFullStatistic (@PathVariable("id") UUID userID) {
        UserFullStatisticDTO userFullStatisticDTO = userWebsiteService.getUserStatisticDTO(userID);

        return new ResponseEntity<>(userFullStatisticDTO, HttpStatus.OK);
    }

    @GetMapping("/details")
    @ResponseBody
    public ResponseEntity<UserDetailsWithStatisticDTO> getUserDetailsWithStatistic (Authentication authentication) {
        UserDetailsWithStatisticDTO userDetailsWithStatisticDTO = userWebsiteService.getUserDetailsOwnWithStatistic(authentication);

        return new ResponseEntity<>(userDetailsWithStatisticDTO, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity<UserDetailsWithStatisticDTO> getSpecificUserDetailsWithStatistic (@PathVariable("id") UUID userID) {
        UserDetailsWithStatisticDTO userDetailsWithStatisticDTO = userWebsiteService.getSpecificUserDetailsWithStatistic(userID);

        return new ResponseEntity<>(userDetailsWithStatisticDTO, HttpStatus.OK);
    }

    @PutMapping("/details")
    @ResponseBody
    public ResponseEntity<UserDetailsDTO> putUserDetailsOwn (@RequestBody @Valid UserRequestDTO userRequestDTO,
                                                             Authentication authentication) {
        UserDetailsDTO userDetailsDTO = userWebsiteService.putUserDetails(userRequestDTO, authentication);

        return new ResponseEntity<>(userDetailsDTO, HttpStatus.OK);
    }

    @PutMapping("/avatarImage")
    @ResponseBody
    public ResponseEntity<UserDetailsDTO> putUserAvatarImage (@RequestParam("file")MultipartFile file,
                                              Authentication authentication) throws IOException, FileExtensionNotAllowedException {
        UserDetailsDTO userDetailsDTO = userWebsiteService.putUserAvatarImage(file, authentication);

        return new ResponseEntity<>(userDetailsDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}/details")
    @ResponseBody
    public ResponseEntity<UserDetailsDTO> putUserDetailsSpecific (@PathVariable("id") UUID userID,
                                                                  @RequestBody @Valid UserRequestDTO userRequestDTO) {
        UserDetailsDTO userDetailsDTO = userWebsiteService.putSpecificUserDetails(userRequestDTO, userID);

        return new ResponseEntity<>(userDetailsDTO, HttpStatus.OK);
    }

    @GetMapping("/actionAvailable")
    @ResponseBody
    public ResponseEntity<List<UserWebsiteAvailableActionDTO>> getUserWebsiteAvailableActions (@RequestParam List<UUID> userIDs,
                                                                                               Authentication authentication) {
        List<UserWebsiteAvailableActionDTO> availableActionDTOs = userWebsiteService.getUserWebsiteAvailableAction(userIDs, authentication);

        return new ResponseEntity<>(availableActionDTOs, HttpStatus.OK);
    }
}
