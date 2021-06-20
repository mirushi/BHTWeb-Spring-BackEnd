package com.bhtcnpm.website.service.Doc.impl;

import com.bhtcnpm.website.model.dto.DocSubject.DocSubjectDTO;
import com.bhtcnpm.website.model.dto.DocSubject.DocSubjectMapper;
import com.bhtcnpm.website.model.entity.DocEntities.DocSubject;
import com.bhtcnpm.website.repository.Doc.DocSubjectRepository;
import com.bhtcnpm.website.service.Doc.DocSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocSubjectServiceImpl implements DocSubjectService {


    private final DocSubjectRepository docSubjectRepository;

    private final DocSubjectMapper docSubjectMapper;

    @Override
    public List<DocSubjectDTO> getDocSubjects() {
        List<DocSubject> queryResult = docSubjectRepository.findAll();

        return docSubjectMapper.docSubjectListToDocSubjectDTOList(queryResult);
    }

    @Override
    public DocSubjectDTO createDocSubject(DocSubjectDTO DocSubjectDTO) {
        DocSubject DocSubject = docSubjectMapper.docSubjectDTOToDocSubject(DocSubjectDTO, new DocSubject());
        DocSubject = docSubjectRepository.save(DocSubject);

        return docSubjectMapper.docSubjectToDocSubjectDTO(DocSubject);
    }

    @Override
    public DocSubjectDTO updateDocSubject(Long DocSubjectId, DocSubjectDTO DocSubjectDTO) {

        DocSubject DocSubject = docSubjectRepository.getOne(DocSubjectId);

        DocSubject = docSubjectMapper.docSubjectDTOToDocSubject(DocSubjectDTO, DocSubject);

        DocSubject = docSubjectRepository.save(DocSubject);

        return docSubjectMapper.docSubjectToDocSubjectDTO(DocSubject);
    }

    @Override
    public Boolean deleteDocSubject(Long DocSubjectId) {
        DocSubject category = docSubjectRepository.getOne(DocSubjectId);
        docSubjectRepository.delete(category);

        return true;
    }

}
