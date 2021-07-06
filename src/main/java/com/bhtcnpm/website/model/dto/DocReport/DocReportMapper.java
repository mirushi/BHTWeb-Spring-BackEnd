package com.bhtcnpm.website.model.dto.DocReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonMapper;
import com.bhtcnpm.website.model.dto.UserWebsite.UserMapper;
import com.bhtcnpm.website.model.entity.DocEntities.report.DocReport;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = Collectors.class,
        uses = {UserMapper.class, ReportReasonMapper.class}
)
public interface DocReportMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "docID", source = "doc.id")
    @Mapping(target = "docTitle", source = "doc.title")
    @Mapping(target = "description", source = "doc.description")
    @Mapping(target = "imageURL", source = "doc.imageURL")
    @Mapping(target = "author", source = "doc.author")
    @Mapping(target = "publishDtm", source = "doc.publishDtm")
    @Mapping(target = "categoryID", source = "doc.category.id")
    @Mapping(target = "categoryName", source = "doc.category.name")
    @Mapping(target = "subjectID", source = "doc.subject.id")
    @Mapping(target = "subjectName", source = "doc.subject.name")
    @Mapping(target = "docFileUploadDTOs", source = "doc.docFileUploads")
    @Mapping(target = "tags", source = "doc.tags")
    @Mapping(target = "reporters", source = "userDocReports")
    @Mapping(target = "reportReasons", source = "userDocReports")
    @Mapping(target = "feedbacks", expression = "java(" +
            "docReport.getUserDocReports()" +
            ".stream()" +
            ".map(obj -> obj.getFeedback())" +
            ".collect(Collectors.toList())" +
            ")")
    @Mapping(target = "reportTime", source = "reportTime")
    @Mapping(target = "resolvedTime", source = "resolvedTime")
    @Mapping(target = "resolvedNote", source = "resolvedNote")
    @Mapping(target = "resolvedBy", source = "resolvedBy")
    @Mapping(target = "actionTaken", source = "actionTaken")
    DocReportDTO docReportToDocReportDTO (DocReport docReport);

    List<DocReportDTO> docReportListToDocReportDTOList (List<DocReport> docReports);

    default DocReportListDTO docReportPageToDocReportListDTO (Page<DocReport> docReports) {
        return DocReportListDTO.builder()
                .docReportDTOs(this.docReportListToDocReportDTOList(docReports.getContent()))
                .totalPages(docReports.getTotalPages())
                .totalElements(docReports.getTotalElements())
                .build();
    }
}
