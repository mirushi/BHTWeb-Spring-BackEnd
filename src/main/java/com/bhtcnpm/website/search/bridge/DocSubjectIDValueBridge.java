package com.bhtcnpm.website.search.bridge;

import com.bhtcnpm.website.model.entity.DocEntities.DocSubject;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class DocSubjectIDValueBridge implements ValueBridge<DocSubject, Long> {

    @Override
    public Long toIndexedValue(DocSubject value, ValueBridgeToIndexedValueContext context) {
        return value == null ? null : value.getId();
    }
}
