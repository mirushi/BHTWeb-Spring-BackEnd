package com.bhtcnpm.website.service.Doc.impl;

import com.bhtcnpm.website.model.dto.DocReport.DocReportListDTO;
import com.bhtcnpm.website.model.dto.DocReport.DocReportMapper;
import com.bhtcnpm.website.model.dto.DocReport.DocReportRequestDTO;
import com.bhtcnpm.website.model.dto.DocReport.DocReportResolveRequestDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.report.*;
import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocReport.DocReportActionType;
import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.repository.Doc.DocReportRepository;
import com.bhtcnpm.website.repository.Doc.DocRepository;
import com.bhtcnpm.website.repository.Doc.UserDocReportRepository;
import com.bhtcnpm.website.repository.ReportReasonRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Doc.DocReportService;
import com.bhtcnpm.website.service.Doc.DocService;
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
public class DocReportServiceImpl implements DocReportService {

    private final DocReportRepository docReportRepository;
    private final UserDocReportRepository userDocReportRepository;
    private final ReportReasonRepository reportReasonRepository;
    private final UserWebsiteRepository userWebsiteRepository;
    private final UserWebsiteService userWebsiteService;
    private final DocRepository docRepository;
    private final DocService docService;

    private final DocReportMapper docReportMapper;

    private static final int PAGE_SIZE = 10;

    @Override
    public boolean createNewReport(Long docID, DocReportRequestDTO dto, Authentication authentication) throws IDNotFoundException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);
        Doc docProxy = docRepository.getOne(docID);
        String feedback = dto.getFeedback();

        //Tìm xem nếu Report của Doc này đã tồn tại thì không tạo mới report nữa.
        DocReport docReport = docReportRepository
                .findByDocAndActionTakenIsNull(docProxy);

        //Doc report chưa tồn tại trên hệ thống hoặc đã được xử lý 1 lần trước đó thì tạo mới.
        if (docReport == null || docReport.getActionTaken() != null) {
            docReport = DocReport.builder()
                    .doc(docProxy)
                    .reportTime(LocalDateTime.now())
                    .build();
        }

        //Cập nhật lại thời gian report mới nhất.
        docReport = docReportRepository.save(docReport);

        UserWebsite reporter = userWebsiteRepository.getOne(userID);

        //Lấy ra tất cả những lý do mà người dùng đã chọn.
        List<Long> reasons = dto.getReasonIds();

        //Thêm user vào danh sách những người đã report bài viết.
        //Kiểm tra xem user đã từng report bài này chưa.
        UserDocReport userDocReport = userDocReportRepository
                .findByUserDocReportId(new UserDocReportId(reporter, docReport));

        //Nếu chưa thì tạo mới.
        if (userDocReport == null) {
            userDocReport = UserDocReport.builder()
                    .userDocReportId(new UserDocReportId(reporter, docReport))
                    .build();
        }

        //Gắn feedback vào entity.
        userDocReport.setFeedback(feedback);

        //Sau đó gán lý do report cho report của user.
        List<ReportReason> reportReasons = dto
                .getReasonIds().stream()
                .map(reportReasonRepository::getOne)
                .collect(Collectors.toList());

        //Ứng với từng reportReason, ta tạo ra field tương ứng.
        //TODO: Kiểm tra bắt buộc report reason IDs không được khai báo trùng nhau. Không sẽ xảy ra lỗi.
        List<DocReportReason> docReportReasons = new ArrayList<>();
        for (ReportReason reportReason : reportReasons) {
            DocReportReasonId docReportReasonId = DocReportReasonId.builder()
                    .reportReason(reportReason)
                    .userDocReport(userDocReport)
                    .build();
            DocReportReason docReportReason = new DocReportReason(docReportReasonId);
            docReportReasons.add(docReportReason);
        }

        //Cuối cùng, override report reason của một user đối với một PostReport.
        if (userDocReport.getReasons() == null) {
            userDocReport.setReasons(docReportReasons);
        } else {
            userDocReport.getReasons().clear();
            userDocReport.getReasons().addAll(docReportReasons);
        }

        //Lưu lại kết quả.
        userDocReportRepository.save(userDocReport);

        return true;
    }

    @Override
    public boolean resolveReport(Long reportID, DocReportResolveRequestDTO dto, Authentication authentication) throws IDNotFoundException {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Optional<DocReport> report = docReportRepository.findById(reportID);
        UserWebsite resolver = userWebsiteRepository.getOne(userID);
        DocReportActionType actionType = dto.getDocReportActionType();
        String resolvedNote = dto.getResolvedNote();

        if (report.isEmpty()) {
            throw new IDNotFoundException();
        }

        DocReport docReport = report.get();
        docReport.setResolvedBy(resolver);
        docReport.setResolvedNote(resolvedNote);
        docReport.setResolvedTime(LocalDateTime.now());
        docReport.setActionTaken(actionType);

        docReportRepository.save(docReport);

        if (DocReportActionType.DELETE.equals(actionType)) {
            docService.deleteDoc(docReport.getDoc().getId(), authentication);
            userWebsiteService.addUserReputationScore(docReport.getDoc().getAuthor().getId(), ReputationType.DOC_REPORTED_REMOVED, 1L);
        }

        return true;
    }

    @Override
    public DocReportListDTO getUserReports(Pageable pageable, Boolean isResolved) {
        //Reset PAGE_SIZE to predefined value.
        pageable = PaginatorUtils.getPageableWithNewPageSize(pageable, PAGE_SIZE);
        Page<DocReport> docReports;

        if (isResolved == null) {
            docReports = docReportRepository.findAll(pageable);
        } else if (isResolved) {
            docReports = docReportRepository.findAllByResolvedTimeNotNull(pageable);
        } else {
            docReports = docReportRepository.findAllByResolvedTimeIsNull(pageable);
        }

        return docReportMapper.docReportPageToDocReportListDTO(docReports);
    }
}
