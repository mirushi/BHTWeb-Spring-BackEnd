package com.bhtcnpm.website.search.bridge;

import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseTopic;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class ExerciseTopicIDValueBridge implements ValueBridge<ExerciseTopic, Long> {

    @Override
    public Long toIndexedValue(ExerciseTopic value, ValueBridgeToIndexedValueContext context) {
        return value == null ? null : value.getId();
    }
}
