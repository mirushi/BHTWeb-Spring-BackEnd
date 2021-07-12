package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.Activity.ActivityDTO;
import com.bhtcnpm.website.model.dto.Activity.ActivityListDTO;
import com.bhtcnpm.website.model.dto.Activity.ActivityMapper;
import com.bhtcnpm.website.model.entity.Activity;
import com.bhtcnpm.website.repository.ActivityRepository;
import com.bhtcnpm.website.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private static final int PAGE_SIZE = 10;

    private final ActivityRepository repository;

    private final ActivityMapper activityMapper;

    @Override
    public ActivityListDTO getAllActivities(Integer paginator) {
        Pageable pageable = PageRequest.of(paginator, PAGE_SIZE, Sort.by("activityDtm").descending());

        Page<Activity> queryResult = repository.findAll(pageable);

        List<ActivityDTO> activityList = queryResult
                .stream()
                .map(activityMapper::activityToActivityDTO)
                .collect(Collectors.toList());

        ActivityListDTO finalResult = new ActivityListDTO(activityList, queryResult.getTotalPages(), queryResult.getTotalElements());

        return finalResult;
    }
}
