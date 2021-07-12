package com.bhtcnpm.website.model.dto.ExerciseCommentReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonMapper;
import com.bhtcnpm.website.model.dto.UserWebsite.UserMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.report.ExerciseCommentReport;
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
public interface ExerciseCommentReportMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "commentID", source = "exerciseComment.id")
    @Mapping(target = "content", source = "exerciseComment.content")
    @Mapping(target = "exerciseID", source = "exerciseComment.exercise.id")
    @Mapping(target = "exerciseTitle", source = "exerciseComment.exercise.title")
    @Mapping(target = "description", source = "exerciseComment.exercise.description")
    @Mapping(target = "author", source = "exerciseComment.author")
    @Mapping(target = "submitDtm", source = "exerciseComment.submitDtm")
    @Mapping(target = "categoryID", source = "exerciseComment.exercise.category.id")
    @Mapping(target = "categoryName", source = "exerciseComment.exercise.category.name")
    @Mapping(target = "subjectID", source = "exerciseComment.exercise.topic.subject.id")
    @Mapping(target = "subjectName", source = "exerciseComment.exercise.topic.subject.name")
    @Mapping(target = "tags", source = "exerciseComment.exercise.tags")
    @Mapping(target = "reporters", source = "userExerciseCommentReports")
    @Mapping(target = "reportReasons", source = "userExerciseCommentReports")
    @Mapping(target = "feedbacks", expression = "java(" +
            "exerciseCommentReport.getUserExerciseCommentReports()" +
            ".stream()" +
            ".map(obj -> obj.getFeedback())" +
            ".collect(Collectors.toList())" +
            ")")
    @Mapping(target = "reportTime", source = "reportTime")
    @Mapping(target = "resolvedTime", source = "resolvedTime")
    @Mapping(target = "resolvedNote", source = "resolvedNote")
    @Mapping(target = "resolvedBy", source = "resolvedBy")
    @Mapping(target = "actionTaken", source = "actionTaken")
    ExerciseCommentReportDTO exerciseCommentReportToExerciseCommentReportDTO (ExerciseCommentReport exerciseCommentReport);

    List<ExerciseCommentReportDTO> exerciseCommentReportListToExerciseCommentReportDTOList (List<ExerciseCommentReport> exerciseCommentReportList);

    default ExerciseCommentReportListDTO exerciseCommentReportPageToExerciseCommentReportListDTO (Page<ExerciseCommentReport> exerciseCommentReports) {
        return ExerciseCommentReportListDTO.builder()
                .exerciseCommentReportDTOs(this.exerciseCommentReportListToExerciseCommentReportDTOList(exerciseCommentReports.getContent()))
                .totalElements(exerciseCommentReports.getTotalElements())
                .totalPages(exerciseCommentReports.getTotalPages())
                .build();
    }
}
