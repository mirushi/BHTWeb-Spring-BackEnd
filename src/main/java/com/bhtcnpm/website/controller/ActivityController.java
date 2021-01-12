package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.Activity.ActivityListDTO;
import com.bhtcnpm.website.model.entity.Activity;
import com.bhtcnpm.website.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/admin/activities")
@Validated
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<ActivityListDTO> getAdminActivities
            (@RequestParam @Min(value = 0, message = "paginator must be greater than or equals to 0") Integer paginator) {

        if (paginator == null) {
            paginator = 0;
        }

        ActivityListDTO activityList = activityService.getAllActivities(paginator);

        return new ResponseEntity<ActivityListDTO>(activityList, HttpStatus.OK);

    }

}
