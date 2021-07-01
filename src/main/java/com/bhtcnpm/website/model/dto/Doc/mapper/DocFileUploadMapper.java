package com.bhtcnpm.website.model.dto.Doc.mapper;

import com.bhtcnpm.website.model.dto.Doc.DocFileUploadDTO;
import com.bhtcnpm.website.model.dto.Doc.DocFileUploadRequestDTO;
import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.repository.Doc.DocFileUploadRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper
public abstract class DocFileUploadMapper {

    protected DocFileUploadRepository docFileUploadRepository;

    public DocFileUpload docFileUploadIDToDocFileUpload (UUID id) {
        if (id == null) {
            return null;
        }
        return docFileUploadRepository.getOne(id);
    }

    public List<DocFileUpload> docFileUploadIDListToDocFileUpload (List<UUID> idList) {
        return docFileUploadRepository.findAllById(idList);
    }

    public List<DocFileUpload> updateDocFileUpload (List<DocFileUpload> docFileUploadList,
                                                    List<DocFileUploadRequestDTO> docFileUploadRequestDTOList) {
        Map<UUID, DocFileUploadRequestDTO> idMapForDocFileUploadRequestDTO = new HashMap<>();

        for (DocFileUploadRequestDTO requestDTO : docFileUploadRequestDTOList) {
            idMapForDocFileUploadRequestDTO.put(requestDTO.getId(), requestDTO);
        }

        for (DocFileUpload docFileUpload : docFileUploadList) {
            DocFileUploadRequestDTO requestDTO = idMapForDocFileUploadRequestDTO.get(docFileUpload.getId());
            docFileUpload.setRank(requestDTO.getRank());
        }

        return docFileUploadList;
    }

    public abstract DocFileUploadDTO docFileUploadToDocFileUploadDTO (DocFileUpload docFileUpload);

    public abstract List<DocFileUploadDTO> docFileUploadListToDocFileUploadDTOList (List<DocFileUpload> docFileUploadList);

    @Autowired
    public void setDocFileUploadRepository (DocFileUploadRepository docFileUploadRepository) {
        this.docFileUploadRepository = docFileUploadRepository;
    }
}
