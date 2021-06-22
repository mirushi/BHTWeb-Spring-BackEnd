package com.bhtcnpm.website.service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginatorUtils {
    public static Pageable getPageableWithNewPageSize (Pageable pageable, Integer pageSize) {
        return PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());
    }
    public static Pageable getPageableWithNewPageSizeAndMoreSort (Pageable pageable, Integer pageSize, Sort moreSort) {
        return PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort().and(moreSort));
    }
}
