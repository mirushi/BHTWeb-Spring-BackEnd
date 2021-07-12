package com.bhtcnpm.website.search.bridge;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseCategory;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class ExerciseCategoryIDValueBridge implements ValueBridge<ExerciseCategory, Long> {

    @Override
    public Long toIndexedValue(ExerciseCategory value, ValueBridgeToIndexedValueContext context) {
        return value == null ? null : value.getId();
    }
}
