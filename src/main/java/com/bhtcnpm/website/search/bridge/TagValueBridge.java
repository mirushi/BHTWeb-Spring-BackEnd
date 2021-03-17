package com.bhtcnpm.website.search.bridge;

import com.bhtcnpm.website.model.entity.Tag;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class TagValueBridge implements ValueBridge<Tag, String> {
    @Override
    public String toIndexedValue(Tag tag, ValueBridgeToIndexedValueContext valueBridgeToIndexedValueContext) {
        return tag == null ? null : tag.getContent();
    }
}
