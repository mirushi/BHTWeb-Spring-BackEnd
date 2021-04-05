package com.bhtcnpm.website.util;

import com.bhtcnpm.website.constant.ApiSortOrder;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;

public class EnumConverter {
    public static SortOrder apiSortOrderToHSearchSortOrder (ApiSortOrder apiSortOrder) {
        if (apiSortOrder == null) {
            return null;
        }
        switch (apiSortOrder) {
            case ASC:{
                return SortOrder.ASC;
            }
            case DESC:{
                return SortOrder.DESC;
            }
        }
        throw new UnsupportedOperationException();
    }
}
