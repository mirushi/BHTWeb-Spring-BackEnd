package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Doc.DocDetailsListDTO;
import com.querydsl.core.types.Predicate;

import javax.validation.constraints.Min;

public interface DocService {

    DocDetailsListDTO getAllDoc (Predicate predicate, @Min(0)Integer paginator);

}
