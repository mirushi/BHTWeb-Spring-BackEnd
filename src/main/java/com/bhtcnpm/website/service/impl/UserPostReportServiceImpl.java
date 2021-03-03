package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.UserPostReport.*;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostReport;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostReportAction.PostReportActionType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.PostRepository;
import com.bhtcnpm.website.repository.UserPostReportRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.service.UserPostReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPostReportServiceImpl implements UserPostReportService {

    private final UserPostReportRepository userPostReportRepository;
    private final UserWebsiteRepository userWebsiteRepository;
    private final PostRepository postRepository;

    private final UserPostReportMapper userPostReportMapper;

    private static final int PAGE_SIZE = 10;

    @Override
    public Boolean createNewReport(Long userId, Long postId, @Valid UserPostReportRequestDTO dto) throws IDNotFoundException {
        Post postProxy = postRepository.getOne(postId);
        UserWebsite reporter = userWebsiteRepository.getOne(userId);
        String reason = dto.getReason();

        UserPostReport entity = UserPostReport.builder()
                .post(postProxy)
                .reporter(reporter)
                .reason(reason)
                .reportTime(LocalDateTime.now())
                .build();

        userPostReportRepository.save(entity);

        return true;
    }

    @Override
    public Boolean resolveReport (Long userId, Long reportId, @Valid UserPostReportResolveRequestDTO dto) throws IDNotFoundException {
        Optional<UserPostReport> report = userPostReportRepository.findById(reportId);
        UserWebsite resolver = userWebsiteRepository.getOne(userId);
        PostReportActionType actionType = dto.getPostReportActionType();
        String resolvedNote = dto.getResolvedNote();

        if (report.isEmpty()) {
            throw new IDNotFoundException();
        }

        UserPostReport userPostReport = report.get();
        userPostReport.setResolvedBy(resolver);
        userPostReport.setResolvedNote(resolvedNote);
        userPostReport.setResolvedTime(LocalDateTime.now());
        userPostReport.setActionTaken(actionType);

        userPostReportRepository.save(userPostReport);

        return true;
    }

    @Override
    public UserPostReportListDTO getUserReports(Pageable pageable) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());

        Page<UserPostReport> userPostReports = userPostReportRepository.findAll(pageable);

        return userPostReportMapper.userPostReportPageToUserPostReportListDTO(userPostReports);
    }
}
