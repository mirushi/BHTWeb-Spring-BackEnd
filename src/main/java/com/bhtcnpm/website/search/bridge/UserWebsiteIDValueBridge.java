package com.bhtcnpm.website.search.bridge;

import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import com.bhtcnpm.website.model.entity.UserWebsite;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

import java.util.UUID;

public class UserWebsiteIDValueBridge implements ValueBridge<UserWebsite, String> {
    @Override
    public String toIndexedValue(UserWebsite value, ValueBridgeToIndexedValueContext context) {
        return value == null ? null : value.getId().toString();
    }
}
