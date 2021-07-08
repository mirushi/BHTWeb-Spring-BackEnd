package com.bhtcnpm.website.service.Post.impl;

import com.bhtcnpm.website.model.dto.UserPostReport.PostReportMapper;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportListDTO;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportRequestDTO;
import com.bhtcnpm.website.model.dto.UserPostReport.UserPostReportResolveRequestDTO;
import com.bhtcnpm.website.model.entity.PostEntities.*;
import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostReportAction.PostReportActionType;
import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.Post.PostReportRepository;
import com.bhtcnpm.website.repository.Post.PostRepository;
import com.bhtcnpm.website.repository.Post.UserPostReportRepository;
import com.bhtcnpm.website.repository.ReportReasonRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Post.UserPostReportService;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPostReportServiceImpl implements UserPostReportService {

    private final PostReportRepository postReportRepository;
    private final UserPostReportRepository userPostReportRepository;
    private final ReportReasonRepository reportReasonRepository;
    private final UserWebsiteRepository userWebsiteRepository;
    private final UserWebsiteService userWebsiteService;
    private final PostRepository postRepository;

    private final PostReportMapper postReportMapper;

    private static final int PAGE_SIZE = 10;

    @Override
    public Boolean createNewReport(Long postID, @Valid UserPostReportRequestDTO dto, Authentication authentication) throws IDNotFoundException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Post postProxy = postRepository.getOne(postID);

        String feedback = dto.getFeedback();

        //Tìm xem nếu Report của Post này đã tồn tại thì không tạo mới report nữa.
        PostReport postReport = postReportRepository
                .findByPostAndActionTakenIsNull(postProxy);

        //Post report chưa tồn tại trên hệ thống hoặc đã được xử lý 1 lần trước đó thì tạo mới.
        if (postReport == null || postReport.getActionTaken() != null) {
            postReport = PostReport.builder()
                    .post(postProxy)
                    .reportTime(LocalDateTime.now())
                    .build();
        }

        //Cập nhật lại thời gian report mới nhất.
        postReport = postReportRepository.save(postReport);

        UserWebsite reporter = userWebsiteRepository.getOne(userID);

        //Lấy ra tất cả những lý do mà người dùng đã chọn.
        List<Long> reasons = dto.getReasonIds();

        //Thêm user vào danh sách những người đã report bài viết.
        //Kiểm tra xem user đã từng report bài này chưa.
        UserPostReport userPostReport = userPostReportRepository
                .findByUserPostReportId(
                        new UserPostReportId(reporter, postReport)
                );

        //Nếu chưa thì tạo mới.
        if (userPostReport == null) {
            userPostReport = UserPostReport.builder()
                    .userPostReportId(
                            new UserPostReportId(reporter, postReport)
                    )
                    .build();
        }

        //Gắn feedback vào entity.
        userPostReport.setFeedback(feedback);

        //Sau đó gán lý do report cho report của user.
        List<ReportReason> reportReasons = dto
                .getReasonIds().stream()
                .map(reportReasonRepository::getOne)
                .collect(Collectors.toList());

        //Ứng với từng reportReason, ta tạo ra field tương ứng.
        //TODO: Kiểm tra bắt buộc report reason IDs không được khai báo trùng nhau. Không sẽ xảy ra lỗi.
        List<PostReportReason> postReportReasons = new ArrayList<>();
        for (ReportReason reportReason : reportReasons) {
            PostReportReasonId postReportReasonId = PostReportReasonId.builder()
                    .reportReason(reportReason)
                    .userPostReport(userPostReport)
                    .build();

            PostReportReason postReportReason = new PostReportReason(postReportReasonId);
            postReportReasons.add(postReportReason);
        }

        //Cuối cùng, override report reason của một user đối với một PostReport.
        if (userPostReport.getReasons() == null) {
            userPostReport.setReasons(postReportReasons);
        } else {
            userPostReport.getReasons().clear();
            userPostReport.getReasons().addAll(postReportReasons);
        }

        //Lưu lại kết quả.
        userPostReportRepository.save(userPostReport);

        return true;
    }

    @Override
    public Boolean resolveReport (Long reportId, @Valid UserPostReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Optional<PostReport> report = postReportRepository.findById(reportId);
        UserWebsite resolver = userWebsiteRepository.getOne(userID);
        PostReportActionType actionType = dto.getPostReportActionType();
        String resolvedNote = dto.getResolvedNote();

        if (report.isEmpty()) {
            throw new IDNotFoundException();
        }

        PostReport postReport = report.get();
        postReport.setResolvedBy(resolver);
        postReport.setResolvedNote(resolvedNote);
        postReport.setResolvedTime(LocalDateTime.now());
        postReport.setActionTaken(actionType);

        postReportRepository.save(postReport);

        if (PostReportActionType.DELETE.equals(actionType)) {
            postRepository.deleteById(postReport.getPost().getId());
            userWebsiteService.addUserReputationScore(postReport.getPost().getAuthor().getId(), ReputationType.POST_REPORTED_REMOVED);
        }

        return true;
    }

    @Override
    public UserPostReportListDTO getUserReports(Pageable pageable, Boolean isResolved) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());
        Page<PostReport> postReports;

        if (isResolved == null) {
            postReports = postReportRepository.findAll(pageable);
        }
        else if (isResolved == true) {
            postReports = postReportRepository.findAllByResolvedTimeNotNull(pageable);
        } else {
            postReports = postReportRepository.findAllByResolvedTimeIsNull(pageable);
        }

        return postReportMapper.userPostReportPageToUserPostReportListDTO(postReports);
    }
}
