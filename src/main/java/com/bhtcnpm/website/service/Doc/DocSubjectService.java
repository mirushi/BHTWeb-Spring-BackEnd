package com.bhtcnpm.website.service.Doc;

import com.bhtcnpm.website.model.dto.DocCategory.DocCategoryDTO;
import com.bhtcnpm.website.model.dto.DocSubject.DocSubjectDTO;
import com.bhtcnpm.website.model.entity.DocEntities.DocSubject;

import java.util.List;

public interface DocSubjectService {
    List<DocSubjectDTO> getDocSubjects ();

    DocSubjectDTO createDocSubject(DocSubjectDTO docSubjectDTO);

    DocSubjectDTO updateDocSubject (Long docSubjectId, DocSubjectDTO docSubjectDTO);

    Boolean deleteDocSubject (Long docSubjectId);
}
