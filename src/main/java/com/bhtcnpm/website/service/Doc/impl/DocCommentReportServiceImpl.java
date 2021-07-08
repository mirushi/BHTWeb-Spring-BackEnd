package com.bhtcnpm.website.service.Doc.impl;

import com.bhtcnpm.website.model.dto.DocCommentReport.DocCommentReportListDTO;
import com.bhtcnpm.website.model.dto.DocCommentReport.DocCommentReportMapper;
import com.bhtcnpm.website.model.dto.DocCommentReport.DocCommentReportRequestDTO;
import com.bhtcnpm.website.model.dto.DocCommentReport.DocCommentReportResolveRequestDTO;
import com.bhtcnpm.website.model.entity.DocCommentEntities.DocComment;
import com.bhtcnpm.website.model.entity.DocCommentEntities.report.*;
import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocCommentReport.DocCommentReportActionType;
import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.Doc.DocCommentReportRepository;
import com.bhtcnpm.website.repository.Doc.DocCommentRepository;
import com.bhtcnpm.website.repository.Doc.UserDocCommentReportRepository;
import com.bhtcnpm.website.repository.ReportReasonRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Doc.DocCommentReportService;
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
public class DocCommentReportServiceImpl implements DocCommentReportService {

    private static final int PAGE_SIZE = 10;
    private final DocCommentRepository docCommentRepository;
    private final DocCommentReportRepository docCommentReportRepository;
    private final UserWebsiteRepository userWebsiteRepository;
    private final UserWebsiteService userWebsiteService;
    private final UserDocCommentReportRepository userDocCommentReportRepository;
    private final ReportReasonRepository reportReasonRepository;
    private final DocCommentReportMapper docCommentReportMapper;

    @Override
    public boolean createNewReport(Long commentID, DocCommentReportRequestDTO dto, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        DocComment docCommentProxy = docCommentRepository.getOne(commentID);

        String feedback = dto.getFeedback();

        //Tìm xem nếu Report của Doc Comment này đã tồn tại thì không tạo mới report nữa.
        DocCommentReport docCommentReport = docCommentReportRepository
                .findByDocCommentAndActionTakenIsNull(docCommentProxy);

        //Doc comment report chưa tồn tại trên hệ thống hoặc đã được xử lý 1 lần trước đó thì tạo mới.
        if (docCommentReport == null || docCommentReport.getActionTaken() != null) {
            docCommentReport = DocCommentReport.builder()
                    .docComment(docCommentProxy)
                    .reportTime(LocalDateTime.now())
                    .build();
        }

        //Cập nhật lại thời gian report mới nhất.
        docCommentReport = docCommentReportRepository.save(docCommentReport);

        UserWebsite reporter = userWebsiteRepository.getOne(userID);

        //Lấy ra tất cả những lý do mà người dùng đã chọn.
        List<Long> reasons = dto.getReasonIds();

        //Thêm user vào danh sách những người đã report bài viết.
        //Kiểm tra xem user đã từng report bài này chưa.
        Optional<UserDocCommentReport> userDocCommentReportOpt = userDocCommentReportRepository
                .findById(new UserDocCommentReportId(reporter, docCommentReport));

        UserDocCommentReport userDocCommentReport;

        //Nếu chưa thì tạo mới.
        if (userDocCommentReportOpt.isEmpty()) {
            userDocCommentReport = UserDocCommentReport.builder()
                    .userDocCommentReportId(new UserDocCommentReportId(reporter, docCommentReport))
                    .build();
        } else {
            userDocCommentReport = userDocCommentReportOpt.get();
        }

        //Gắn feedback vào entity.
        userDocCommentReport.setFeedback(feedback);

        //Sau đó gán lý do report cho report của user.
        List<ReportReason> reportReasons = dto
                .getReasonIds().stream()
                .map(reportReasonRepository::getOne)
                .collect(Collectors.toList());

        //Ứng với từng reportReason, ta tạo ra field tương ứng.
        //TODO: Kiểm tra bắt buộc report reason IDs không được khai báo trùng nhau. Không sẽ xảy ra lỗi.
        List<DocCommentReportReason> docCommentReportReasons = new ArrayList<>();
        for (ReportReason reportReason : reportReasons) {
            DocCommentReportReasonId docCommentReportReasonId = DocCommentReportReasonId.builder()
                    .reportReason(reportReason)
                    .userDocCommentReport(userDocCommentReport)
                    .build();
            DocCommentReportReason docCommentReportReason = new DocCommentReportReason(docCommentReportReasonId);
            docCommentReportReasons.add(docCommentReportReason);
        }

        //Cuối cùng, override report reason của một user đối với một PostReport.
        if (userDocCommentReport.getReasons() == null){
            userDocCommentReport.setReasons(docCommentReportReasons);
        } else {
            userDocCommentReport.getReasons().clear();
            userDocCommentReport.getReasons().addAll(docCommentReportReasons);
        }

        //Lưu lại kết quả.
        userDocCommentReportRepository.save(userDocCommentReport);

        return true;
    }

    @Override
    public boolean resolveReport(Long reportID, DocCommentReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Optional<DocCommentReport> report = docCommentReportRepository.findById(reportID);
        UserWebsite resolver = userWebsiteRepository.getOne(userID);
        DocCommentReportActionType actionType = dto.getDocCommentReportActionType();
        String resolvedNote = dto.getResolvedNote();

        if (report.isEmpty()) {
            throw new IDNotFoundException();
        }

        DocCommentReport docCommentReport = report.get();
        docCommentReport.setResolvedBy(resolver);
        docCommentReport.setResolvedNote(resolvedNote);
        docCommentReport.setResolvedTime(LocalDateTime.now());
        docCommentReport.setActionTaken(actionType);

        docCommentReportRepository.save(docCommentReport);

        if (DocCommentReportActionType.DELETE.equals(actionType)) {
            docCommentRepository.deleteById(docCommentReport.getDocComment().getId());
            userWebsiteService.addUserReputationScore(docCommentReport.getDocComment().getAuthor().getId(), ReputationType.COMMENT_REPORTED_REMOVED);
        }

        return true;
    }

    @Override
    public DocCommentReportListDTO getUserReports(Pageable pageable, Boolean isResolved) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, PAGE_SIZE);
        Page<DocCommentReport> docCommentReports;

        if (isResolved == null) {
            docCommentReports = docCommentReportRepository.findAll(pageable);
        } else if (isResolved) {
            docCommentReports = docCommentReportRepository.findAllByResolvedTimeNotNull(pageable);
        } else {
            docCommentReports = docCommentReportRepository.findAllByResolvedTimeIsNull(pageable);
        }

        return docCommentReportMapper.docCommentReportPageToDocCommentReportListDTO(docCommentReports);
    }
}
