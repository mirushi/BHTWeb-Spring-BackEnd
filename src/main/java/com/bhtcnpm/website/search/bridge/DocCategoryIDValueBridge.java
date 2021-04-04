package com.bhtcnpm.website.search.bridge;

import com.bhtcnpm.website.model.entity.DocEntities.DocCategory;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class DocCategoryIDValueBridge implements ValueBridge<DocCategory, Long> {

    @Override
    public Long toIndexedValue(DocCategory value, ValueBridgeToIndexedValueContext context) {
        return value == null ? null : value.getId();
    }
}
