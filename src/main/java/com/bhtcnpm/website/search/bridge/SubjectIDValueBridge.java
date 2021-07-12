package com.bhtcnpm.website.search.bridge;

import com.bhtcnpm.website.model.entity.SubjectEntities.Subject;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class SubjectIDValueBridge implements ValueBridge<Subject, Long> {

    @Override
    public Long toIndexedValue(Subject value, ValueBridgeToIndexedValueContext context) {
        return value == null ? null : value.getId();
    }
}
