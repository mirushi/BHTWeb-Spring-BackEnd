package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.UserExerciseReport.ExerciseReportMapper;
import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportListDTO;
import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportRequestDTO;
import com.bhtcnpm.website.model.dto.UserExerciseReport.UserExerciseReportResolveRequestDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.*;
import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseReportAction.ExerciseReportActionType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.Exercise.ExerciseReportRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.Exercise.UserExerciseReportRepository;
import com.bhtcnpm.website.repository.ReportReasonRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Exercise.ExerciseReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseReportServiceImpl implements ExerciseReportService {

    private final ExerciseReportRepository exerciseReportRepository;
    private final UserExerciseReportRepository userExerciseReportRepository;
    private final ReportReasonRepository reportReasonRepository;
    private final UserWebsiteRepository userWebsiteRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseReportMapper exerciseReportMapper;

    private static final int PAGE_SIZE = 10;

    @Override
    public Boolean createNewReport(Long exerciseID, UserExerciseReportRequestDTO dto, Authentication authentication) throws IDNotFoundException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Exercise exerciseProxy = exerciseRepository.getOne(exerciseID);

        String feedback = dto.getFeedback();

        //Tìm xem nếu Report của Exercise này đã tồn tại thì không tạo mới report nữa.
        ExerciseReport exerciseReport = exerciseReportRepository.findByExerciseAndActionTakenIsNull(exerciseProxy);

        //Exercise report chưa tồn tại trên hệ thống hoặc đã được xử lý 1 lần trước đó thì tạo mới.
        if (exerciseReport == null || exerciseReport.getActionTaken() != null) {
            exerciseReport = ExerciseReport.builder()
                    .exercise(exerciseProxy)
                    .reportTime(LocalDateTime.now())
                    .build();
        }

        //Cập nhật lại thời gian report mới nhất.
        exerciseReport = exerciseReportRepository.save(exerciseReport);

        UserWebsite reporter = userWebsiteRepository.getOne(userID);

        //Lấy ra tất cả những lý do mà người dùng đã chọn.
        List<Long> reasons = dto.getReasonIds();

        //Thêm user vào danh sách những người đã report exercise.
        //Kiểm tra xem user đã từng report exercise này chưa.
        UserExerciseReport userExerciseReport = userExerciseReportRepository.findByUserExerciseReportId(
                new UserExerciseReportId(reporter, exerciseReport)
        );

        //Nếu chưa thì tạo mới.
        if (userExerciseReport == null) {
            userExerciseReport = UserExerciseReport.builder()
                    .userExerciseReportId(
                            new UserExerciseReportId(reporter, exerciseReport)
                    ).build();
        }

        //Gắn feedback vào entity.
        userExerciseReport.setFeedback(feedback);

        //Sau đó gán lý do report cho report của user.
        List<ReportReason> reportReasons = dto
                .getReasonIds().stream()
                .map(reportReasonRepository::getOne)
                .collect(Collectors.toList());

        //Ứng với từng reportReason, ta tạo ra field tương ứng.
        List<ExerciseReportReason> exerciseReportReasons = new ArrayList<>();
        for (ReportReason reportReason : reportReasons) {
            ExerciseReportReasonId exerciseReportReasonId = ExerciseReportReasonId.builder()
                    .reportReason(reportReason)
                    .userExerciseReport(userExerciseReport)
                    .build();
            ExerciseReportReason exerciseReportReason = new ExerciseReportReason(exerciseReportReasonId);
            exerciseReportReasons.add(exerciseReportReason);
        }

        //Cuối cùng, override report reason của một user đối với một PostReport.
        if (userExerciseReport.getReasons() == null) {
            userExerciseReport.setReasons(exerciseReportReasons);
        } else {
            userExerciseReport.getReasons().clear();
            userExerciseReport.getReasons().addAll(exerciseReportReasons);
        }

        //Lưu lại kết quả.
        userExerciseReportRepository.save(userExerciseReport);

        return true;
    }

    @Override
    public Boolean resolveReport(Long reportID, UserExerciseReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Optional<ExerciseReport> report = exerciseReportRepository.findById(reportID);
        UserWebsite resolver = userWebsiteRepository.getOne(userID);
        ExerciseReportActionType actionType = dto.getExerciseReportActionType();
        String resolvedNote = dto.getResolvedNote();

        if (report.isEmpty()) {
            throw new IDNotFoundException();
        }

        ExerciseReport exerciseReport = report.get();
        exerciseReport.setResolvedBy(resolver);
        exerciseReport.setResolvedNote(resolvedNote);
        exerciseReport.setResolvedTime(LocalDateTime.now());
        exerciseReport.setActionTaken(actionType);

        exerciseReportRepository.save(exerciseReport);

        return true;
    }

    @Override
    public UserExerciseReportListDTO getUserReports(Pageable pageable, Boolean isResolved) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());

        Page<ExerciseReport> exerciseReports;

        if (isResolved == null) {
            exerciseReports = exerciseReportRepository.findAll(pageable);
        } else if (isResolved) {
            exerciseReports = exerciseReportRepository.findAllByResolvedTimeNotNull(pageable);
        } else {
            exerciseReports = exerciseReportRepository.findAllByResolvedTimeIsNull(pageable);
        }

        return exerciseReportMapper.userExerciseReportPageToUserExerciseReportListDTO(exerciseReports);
    }
}
