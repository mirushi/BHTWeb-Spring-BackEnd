package com.bhtcnpm.website.search.bridge;

import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class PostCategoryValueBridge implements ValueBridge<PostCategory, Long> {

    @Override
    public Long toIndexedValue(PostCategory value, ValueBridgeToIndexedValueContext context) {
        return value == null ? null : value.getId();
    }
}
