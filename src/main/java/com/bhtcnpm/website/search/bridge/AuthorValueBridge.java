package com.bhtcnpm.website.search.bridge;

import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import com.bhtcnpm.website.model.entity.UserWebsite;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class AuthorValueBridge implements ValueBridge<UserWebsite, Long> {
    @Override
    public Long toIndexedValue(UserWebsite value, ValueBridgeToIndexedValueContext context) {
        return value == null ? null : value.getId();
    }
}
