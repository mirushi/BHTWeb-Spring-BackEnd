package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonDTO;
import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonMapper;
import com.bhtcnpm.website.model.entity.ReportReason.ReportReason;
import com.bhtcnpm.website.repository.ReportReasonRepository;
import com.bhtcnpm.website.service.ReportReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportReasonServiceImpl implements ReportReasonService {

    private final ReportReasonRepository reportReasonRepository;

    private final ReportReasonMapper reportReasonMapper;

    @Override
    public List<ReportReasonDTO> getAllReportReason() {
        List <ReportReason> reportReasons = reportReasonRepository.findAll();
        List<ReportReasonDTO> dtoList = reportReasonMapper.reportReasonListToReportReasonDTOList(reportReasons);
        return dtoList;
    }
}
