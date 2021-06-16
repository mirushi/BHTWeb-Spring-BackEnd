package com.bhtcnpm.website.model.dto.Doc.mapper;

import com.bhtcnpm.website.model.dto.Doc.DocDownloadInfoDTO;
import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocDownloadInfoMapper {
    DocDownloadInfoMapper INSTANCE = Mappers.getMapper(DocDownloadInfoMapper.class);

    DocDownloadInfoDTO docFileUploadToDocDownloadInfoDTO (DocFileUpload docFileUpload);
}
