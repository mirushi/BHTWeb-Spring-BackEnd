package com.bhtcnpm.website.service.ExerciseComment;

import com.bhtcnpm.website.model.dto.ExerciseCommentReport.ExerciseCommentReportListDTO;
import com.bhtcnpm.website.model.dto.ExerciseCommentReport.ExerciseCommentReportMapper;
import com.bhtcnpm.website.model.dto.ExerciseCommentReport.ExerciseCommentReportRequestDTO;
import com.bhtcnpm.website.model.dto.ExerciseCommentReport.ExerciseCommentReportResolveRequestDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseComment;
import com.bhtcnpm.website.model.entity.ExerciseEntities.report.*;
import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseCommentReport.ExerciseCommentReportActionType;
import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.Exercise.ExerciseCommentReportRepository;
import com.bhtcnpm.website.repository.Exercise.UserExerciseCommentReportRepository;
import com.bhtcnpm.website.repository.ExerciseComment.ExerciseCommentRepository;
import com.bhtcnpm.website.repository.ReportReasonRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.UserWebsiteService;
import com.bhtcnpm.website.service.util.PaginatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
public class ExerciseCommentReportServiceImpl implements ExerciseCommentReportService {

    private static final int PAGE_SIZE = 10;
    private final ExerciseCommentRepository exerciseCommentRepository;
    private final ExerciseCommentService exerciseCommentService;
    private final ExerciseCommentReportRepository exerciseCommentReportRepository;
    private final UserWebsiteRepository userWebsiteRepository;
    private final UserWebsiteService userWebsiteService;
    private final UserExerciseCommentReportRepository userExerciseCommentReportRepository;
    private final ReportReasonRepository reportReasonRepository;
    private final ExerciseCommentReportMapper exerciseCommentReportMapper;

    @Override
    public boolean createNewReport(Long commentID, ExerciseCommentReportRequestDTO dto, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        ExerciseComment exerciseCommentProxy = exerciseCommentRepository.getOne(commentID);

        String feedback = dto.getFeedback();

        //Tìm xem nếu Report của Exercise Comment này đã tồn tại thì không tạo mới report nữa.
        ExerciseCommentReport exerciseCommentReport = exerciseCommentReportRepository
                .findByExerciseCommentAndActionTakenIsNull(exerciseCommentProxy);

        //Exercise comment report chưa tồn tại trên hệ thống hoặc đã được xử lý 1 lần trước đó thì tạo mới.
        if (exerciseCommentReport == null || exerciseCommentReport.getActionTaken() != null) {
            exerciseCommentReport = ExerciseCommentReport.builder()
                    .exerciseComment(exerciseCommentProxy)
                    .reportTime(LocalDateTime.now())
                    .build();
        }

        //Cập nhật lại thời gian report mới nhất.
        exerciseCommentReport = exerciseCommentReportRepository.save(exerciseCommentReport);

        UserWebsite reporter = userWebsiteRepository.getOne(userID);

        //Lấy ra tất cả những lý do mà người dùng đã chọn.
        List<Long> reasons = dto.getReasonIds();

        //Thêm user vào danh sách những người đã report exercise comment.
        //Kiểm tra xem user đã từng report bài này chưa.
        Optional<UserExerciseCommentReport> userExerciseCommentReportOpt = userExerciseCommentReportRepository
                .findById(new UserExerciseCommentReportId(reporter, exerciseCommentReport));

        UserExerciseCommentReport userExerciseCommentReport;

        //Nếu chưa thì tạo mới.
        if (userExerciseCommentReportOpt.isEmpty()) {
            userExerciseCommentReport = UserExerciseCommentReport.builder()
                    .userExerciseCommentReportId(new UserExerciseCommentReportId(reporter, exerciseCommentReport))
                    .build();
        } else {
            userExerciseCommentReport = userExerciseCommentReportOpt.get();
        }

        //Gắn feedback vào entity.
        userExerciseCommentReport.setFeedback(feedback);

        //Sau đó gán lý do report cho report của user.
        List<ReportReason> reportReasons = dto
                .getReasonIds().stream()
                .map(reportReasonRepository::getOne)
                .collect(Collectors.toList());

        //Ứng với từng reportReason, ta tạo ra field tương ứng.
        //TODO: Kiểm tra bắt buộc report reason IDs không được khai báo trùng nhau. Không sẽ xảy ra lỗi.
        List<ExerciseCommentReportReason> exerciseCommentReportReasons = new ArrayList<>();
        for (ReportReason reportReason : reportReasons) {
            ExerciseCommentReportReasonId exerciseCommentReportReasonId = ExerciseCommentReportReasonId.builder()
                    .reportReason(reportReason)
                    .userExerciseCommentReport(userExerciseCommentReport)
                    .build();
            ExerciseCommentReportReason exerciseCommentReportReason = new ExerciseCommentReportReason(exerciseCommentReportReasonId);
            exerciseCommentReportReasons.add(exerciseCommentReportReason);
        }

        //Cuối cùng, override report reason của một user đối với một PostReport.
        if (userExerciseCommentReport.getReasons() == null) {
            userExerciseCommentReport.setReasons(exerciseCommentReportReasons);
        } else {
            userExerciseCommentReport.getReasons().clear();
            userExerciseCommentReport.getReasons().addAll(exerciseCommentReportReasons);
        }

        //Lưu lại kết quả.
        userExerciseCommentReportRepository.save(userExerciseCommentReport);

        return true;
    }

    @Override
    public boolean resolveReport(Long reportID, ExerciseCommentReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Optional<ExerciseCommentReport> report = exerciseCommentReportRepository.findById(reportID);
        UserWebsite resolver = userWebsiteRepository.getOne(userID);
        ExerciseCommentReportActionType actionType = dto.getExerciseCommentReportActionType();
        String resolvedNote = dto.getResolvedNote();

        if (report.isEmpty()) {
            throw new IDNotFoundException();
        }

        ExerciseCommentReport exerciseCommentReport = report.get();
        exerciseCommentReport.setResolvedBy(resolver);
        exerciseCommentReport.setResolvedNote(resolvedNote);
        exerciseCommentReport.setResolvedTime(LocalDateTime.now());
        exerciseCommentReport.setActionTaken(actionType);

        exerciseCommentReportRepository.save(exerciseCommentReport);

        if (ExerciseCommentReportActionType.DELETE.equals(actionType)) {
            exerciseCommentService.deleteExerciseComment(exerciseCommentReport.getExerciseComment().getId());
            userWebsiteService.addUserReputationScore(exerciseCommentReport.getExerciseComment().getAuthor().getId(), ReputationType.COMMENT_REPORTED_REMOVED, 1L);
        }

        return true;
    }

    @Override
    public ExerciseCommentReportListDTO getUserReports(Pageable pageable, Boolean isResolved) {
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, PAGE_SIZE);
        Page<ExerciseCommentReport> exerciseCommentReports;

        if (isResolved == null) {
            exerciseCommentReports = exerciseCommentReportRepository.findAll(pageable);
        } else if (isResolved) {
            exerciseCommentReports = exerciseCommentReportRepository.findAllByResolvedTimeNotNull(pageable);
        } else {
            exerciseCommentReports = exerciseCommentReportRepository.findAllByResolvedTimeIsNull(pageable);
        }

        return exerciseCommentReportMapper.exerciseCommentReportPageToExerciseCommentReportListDTO(exerciseCommentReports);
    }
}
