package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Activity.ActivityListDTO;
import com.bhtcnpm.website.model.entity.Activity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ActivityService {
    ActivityListDTO getAllActivities (Integer paginator);
}
