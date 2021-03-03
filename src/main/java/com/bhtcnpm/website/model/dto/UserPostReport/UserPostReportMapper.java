package com.bhtcnpm.website.model.dto.UserPostReport;

import com.bhtcnpm.website.model.entity.PostEntities.UserPostReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public abstract class UserPostReportMapper {

    public static final UserPostReportMapper INSTANCE = Mappers.getMapper(UserPostReportMapper.class);

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "reporterId", source = "reporter.id")
    public abstract UserPostReportDTO userPostReportToUserPostReportDTO (UserPostReport userPostReport);

    public abstract List<UserPostReportDTO> userPostReportListToUserPostReportDTOList (List<UserPostReport> userPostReports);

    public UserPostReportListDTO userPostReportPageToUserPostReportListDTO (Page<UserPostReport> userPostReports) {
        UserPostReportListDTO dto = new UserPostReportListDTO();
        dto.setUserPostReportDTOs(INSTANCE.userPostReportListToUserPostReportDTOList(userPostReports.getContent()));
        dto.setTotalPages(userPostReports.getTotalPages());
        dto.setTotalElements(userPostReports.getTotalElements());

        return dto;
    }
}
