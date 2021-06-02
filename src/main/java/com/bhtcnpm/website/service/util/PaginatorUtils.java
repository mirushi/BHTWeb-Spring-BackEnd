package com.bhtcnpm.website.service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginatorUtils {
    public static Pageable getPageableWithNewPageSize (Pageable pageable, Integer pageSize) {
        return PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());
    }
}
