package com.bhtcnpm.website.model.dto.UserExerciseReport;

import com.bhtcnpm.website.model.dto.ReportReason.ReportReasonMapper;
import com.bhtcnpm.website.model.dto.UserWebsite.UserMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseReport;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = Collectors.class,
        uses = {UserMapper.class,
                ReportReasonMapper.class})
public interface ExerciseReportMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "exerciseID", source = "exercise.id")
    @Mapping(target = "title", source = "exercise.title")
    @Mapping(target = "description", source = "exercise.description")
    @Mapping(target = "suggestedDuration", source = "exercise.suggestedDuration")
    @Mapping(target = "categoryID", source = "exercise.category.id")
    @Mapping(target = "categoryName", source = "exercise.category.name")
    @Mapping(target = "topicID", source = "exercise.topic.id")
    @Mapping(target = "topicName", source = "exercise.topic.name")
    @Mapping(target = "subjectID", source = "exercise.topic.subject.id")
    @Mapping(target = "subjectName", source = "exercise.topic.subject.name")
    @Mapping(target = "author", source = "exercise.author")
    @Mapping(target = "reporters",
            source = "exerciseReport.userExerciseReports.")
    @Mapping(target = "reportReasons", source = "exerciseReport.userExerciseReports")
    @Mapping(target = "feedbacks", expression = "java(" +
            "exerciseReport.getUserExerciseReports()" +
            ".stream()" +
            ".map(obj -> obj.getFeedback())" +
            ".collect(Collectors.toList())" +
            ")")
    @Mapping(target = "reportTime", source = "reportTime")
    @Mapping(target = "resolvedTime", source = "resolvedTime")
    @Mapping(target = "resolvedNote", source = "resolvedNote")
    @Mapping(target = "resolvedBy", source = "resolvedBy")
    @Mapping(target = "actionTaken", source = "actionTaken")
    ExerciseReportDTO exerciseReportToExerciseReportDTO (ExerciseReport exerciseReport);

    List<ExerciseReportDTO> exerciseReportListToExerciseReportDTOList (List<ExerciseReport> exerciseReports);

    default UserExerciseReportListDTO userExerciseReportPageToUserExerciseReportListDTO (Page<ExerciseReport> userExerciseReports) {
        UserExerciseReportListDTO dto = UserExerciseReportListDTO.builder()
                .exerciseReportDTOs(this.exerciseReportListToExerciseReportDTOList(userExerciseReports.getContent()))
                .totalPages(userExerciseReports.getTotalPages())
                .totalElements(userExerciseReports.getTotalElements())
                .build();
        return dto;
    }
}
