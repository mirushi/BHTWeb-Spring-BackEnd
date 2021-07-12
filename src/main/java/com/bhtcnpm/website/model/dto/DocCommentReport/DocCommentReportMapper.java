package com.bhtcnpm.website.model.dto.DocCommentReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonMapper;
import com.bhtcnpm.website.model.dto.UserWebsite.UserMapper;
import com.bhtcnpm.website.model.entity.DocCommentEntities.report.DocCommentReport;
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
public interface DocCommentReportMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "commentID", source = "docComment.id")
    @Mapping(target = "content", source = "docComment.content")
    @Mapping(target = "docID", source = "docComment.doc.id")
    @Mapping(target = "docTitle", source = "docComment.doc.title")
    @Mapping(target = "description", source = "docComment.doc.description")
    @Mapping(target = "imageURL", source = "docComment.doc.imageURL")
    @Mapping(target = "author", source = "docComment.author")
    @Mapping(target = "submitDtm", source = "docComment.submitDtm")
    @Mapping(target = "categoryID", source = "docComment.doc.category.id")
    @Mapping(target = "categoryName", source = "docComment.doc.category.name")
    @Mapping(target = "subjectID", source = "docComment.doc.subject.id")
    @Mapping(target = "subjectName", source = "docComment.doc.subject.name")
    @Mapping(target = "docFileUploadDTOs", source = "docComment.doc.docFileUploads")
    @Mapping(target = "tags", source = "docComment.doc.tags")
    @Mapping(target = "reporters", source = "userDocCommentReports")
    @Mapping(target = "reportReasons", source = "userDocCommentReports")
    @Mapping(target = "feedbacks", expression = "java(" +
            "docCommentReport.getUserDocCommentReports()" +
            ".stream()" +
            ".map(obj -> obj.getFeedback())" +
            ".collect(Collectors.toList())" +
            ")")
    @Mapping(target = "reportTime", source = "reportTime")
    @Mapping(target = "resolvedTime", source = "resolvedTime")
    @Mapping(target = "resolvedNote", source = "resolvedNote")
    @Mapping(target = "resolvedBy", source = "resolvedBy")
    @Mapping(target = "actionTaken", source = "actionTaken")
    DocCommentReportDTO docCommentReportToDocCommentReportDTO (DocCommentReport docCommentReport);

    List<DocCommentReportDTO> docCommentReportListToDocCommentReportDTOList (List<DocCommentReport> docCommentReports);

    default DocCommentReportListDTO docCommentReportPageToDocCommentReportListDTO(Page<DocCommentReport> docCommentReports) {
        return DocCommentReportListDTO.builder()
                .docCommentReportDTOs(this.docCommentReportListToDocCommentReportDTOList(docCommentReports.getContent()))
                .totalElements(docCommentReports.getTotalElements())
                .totalPages(docCommentReports.getTotalPages())
                .build();
    }
}
