package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.UserPostReport.*;
import com.bhtcnpm.website.model.entity.PostEntities.*;
import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostReportAction.PostReportActionType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.*;
import com.bhtcnpm.website.service.UserPostReportService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPostReportServiceImpl implements UserPostReportService {

    private final PostReportRepository postReportRepository;
    private final UserPostReportRepository userPostReportRepository;
    private final ReportReasonRepository reportReasonRepository;
    private final UserWebsiteRepository userWebsiteRepository;
    private final PostRepository postRepository;

    private final PostReportMapper postReportMapper;

    private static final int PAGE_SIZE = 10;

    @Override
    public Boolean createNewReport(Long userId,
                                   Long postId,
                                   @Valid UserPostReportRequestDTO dto) throws IDNotFoundException {
        Post postProxy = postRepository.getOne(postId);

        String feedback = dto.getFeedback();

        //Tìm xem nếu Report của Post này đã tồn tại thì không tạo mới report nữa.
        PostReport postReport = postReportRepository
                .findByPost(postProxy);

        //Post report chưa tồn tại trên hệ thống thì tạo mới.
        if (postReport == null) {
            postReport = PostReport.builder()
                    .post(postProxy)
                    .reportTime(LocalDateTime.now())
                    .build();
            postReportRepository.save(postReport);
        }

        UserWebsite reporter = userWebsiteRepository.getOne(userId);

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
    public Boolean resolveReport (Long userId, Long reportId, @Valid UserPostReportResolveRequestDTO dto) throws IDNotFoundException {
        Optional<PostReport> report = postReportRepository.findById(reportId);
        UserWebsite resolver = userWebsiteRepository.getOne(userId);
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

        //Initialize lazy proxy.
        //TODO: N+1 issue. Fix or not ?
        for (PostReport report : postReports.getContent()) {
            Hibernate.initialize(report.getUserPostReports());
        }

        return postReportMapper.userPostReportPageToUserPostReportListDTO(postReports);
    }
}
