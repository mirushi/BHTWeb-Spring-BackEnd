package com.bhtcnpm.website.service.Post.impl;

import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportListDTO;
import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportMapper;
import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportRequestDTO;
import com.bhtcnpm.website.model.dto.PostCommentReport.PostCommentReportResolveRequestDTO;
import com.bhtcnpm.website.model.entity.PostCommentEntities.*;
import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostCommentReportAction.PostCommentReportActionType;
import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.Post.PostCommentReportRepository;
import com.bhtcnpm.website.repository.Post.PostCommentRepository;
import com.bhtcnpm.website.repository.Post.UserPostCommentReportRepository;
import com.bhtcnpm.website.repository.ReportReasonRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Post.PostCommentReportService;
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
public class PostCommentReportServiceImpl implements PostCommentReportService {

    private static final int PAGE_SIZE = 10;

    private final PostCommentRepository postCommentRepository;

    private final PostCommentReportRepository postCommentReportRepository;

    private final UserWebsiteRepository userWebsiteRepository;

    private final UserWebsiteService userWebsiteService;

    private final UserPostCommentReportRepository userPostCommentReportRepository;

    private final ReportReasonRepository reportReasonRepository;

    private final PostCommentReportMapper postCommentReportMapper;

    @Override
    public boolean createNewReport(Long commentID, PostCommentReportRequestDTO dto, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        PostComment postCommentProxy = postCommentRepository.getOne(commentID);

        String feedback = dto.getFeedback();

        //Tìm xem nếu Report của Post này đã tồn tại thì không tạo mới report nữa.
        PostCommentReport postCommentReport = postCommentReportRepository
                .findByPostCommentAndActionTakenIsNull(postCommentProxy);

        //Post report chưa tồn tại trên hệ thống hoặc đã được xử lý 1 lần trước đó thì tạo mới.
        if (postCommentReport == null || postCommentReport.getActionTaken() != null) {
            postCommentReport = PostCommentReport.builder()
                    .postComment(postCommentProxy)
                    .reportTime(LocalDateTime.now())
                    .build();
        }

        //Cập nhật lại thời gian report mới nhất.
        postCommentReport = postCommentReportRepository.save(postCommentReport);

        UserWebsite reporter = userWebsiteRepository.getOne(userID);

        //Lấy ra tất cả những lý do mà người dùng đã chọn.
        List<Long> reasons = dto.getReasonIds();

        //Thêm user vào danh sách những người đã report bài viết.
        //Kiểm tra xem user đã từng report bài này chưa.
        Optional<UserPostCommentReport> userPostCommentReportOpt = userPostCommentReportRepository
                .findById(new UserPostCommentReportId(reporter, postCommentReport));

        UserPostCommentReport userPostCommentReport;

        //Nếu chưa thì tạo mới.
        if (userPostCommentReportOpt.isEmpty()) {
            userPostCommentReport = UserPostCommentReport.builder()
                    .userPostCommentReportId(new UserPostCommentReportId(reporter, postCommentReport))
                    .build();
        } else {
            userPostCommentReport = userPostCommentReportOpt.get();
        }

        //Gắn feedback vào entity.
        userPostCommentReport.setFeedback(feedback);

        //Sau đó gán lý do report cho report của user.
        List<ReportReason> reportReasons = dto
                .getReasonIds().stream()
                .map(reportReasonRepository::getOne)
                .collect(Collectors.toList());

        //Ứng với từng reportReason, ta tạo ra field tương ứng.
        //TODO: Kiểm tra bắt buộc report reason IDs không được khai báo trùng nhau. Không sẽ xảy ra lỗi.
        List<PostCommentReportReason> postCommentReportReasons = new ArrayList<>();
        for (ReportReason reportReason : reportReasons) {
            PostCommentReportReasonId postCommentReportReasonId = PostCommentReportReasonId.builder()
                    .reportReason(reportReason)
                    .userPostCommentReport(userPostCommentReport)
                    .build();
            PostCommentReportReason postCommentReportReason = new PostCommentReportReason(postCommentReportReasonId);
            postCommentReportReasons.add(postCommentReportReason);
        }

        //Cuối cùng, override report reason của một user đối với một PostReport.
        if (userPostCommentReport.getReasons() == null) {
            userPostCommentReport.setReasons(postCommentReportReasons);
        } else {
            userPostCommentReport.getReasons().clear();
            userPostCommentReport.getReasons().addAll(postCommentReportReasons);
        }

        //Lưu lại kết quả.
        userPostCommentReportRepository.save(userPostCommentReport);

        return true;
    }

    @Override
    public boolean resolveReport(Long reportID, PostCommentReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Optional<PostCommentReport> report = postCommentReportRepository.findById(reportID);
        UserWebsite resolver = userWebsiteRepository.getOne(userID);
        PostCommentReportActionType actionType = dto.getPostCommentReportActionType();
        String resolvedNote = dto.getResolvedNote();

        if (report.isEmpty()) {
            throw new IDNotFoundException();
        }

        PostCommentReport postCommentReport = report.get();
        postCommentReport.setResolvedBy(resolver);
        postCommentReport.setResolvedNote(resolvedNote);
        postCommentReport.setResolvedTime(LocalDateTime.now());
        postCommentReport.setActionTaken(actionType);

        postCommentReportRepository.save(postCommentReport);

        if (PostCommentReportActionType.DELETE.equals(actionType)) {
            postCommentRepository.deleteById(postCommentReport.getPostComment().getId());
            userWebsiteService.addUserReputationScore(postCommentReport.getPostComment().getAuthor().getId(), ReputationType.COMMENT_REPORTED_REMOVED);
        }

        return true;
    }

    @Override
    public PostCommentReportListDTO getUserReports(Pageable pageable, Boolean isResolved) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, PAGE_SIZE);
        Page<PostCommentReport> postCommentReports;

        if (isResolved == null) {
            postCommentReports = postCommentReportRepository.findAll(pageable);
        } else if (isResolved) {
            postCommentReports = postCommentReportRepository.findAllByResolvedTimeNotNull(pageable);
        } else {
            postCommentReports = postCommentReportRepository.findAllByResolvedTimeIsNull(pageable);
        }

        return postCommentReportMapper.userPostCommentReportPageToUserPostCommentReportListDTO(postCommentReports);
    }
}
