package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Activity.ActivityListDTO;

public interface ActivityService {
    ActivityListDTO getAllActivities (Integer paginator);
}
