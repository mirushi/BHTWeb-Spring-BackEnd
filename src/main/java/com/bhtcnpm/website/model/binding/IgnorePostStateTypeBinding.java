package com.bhtcnpm.website.model.binding;

import com.bhtcnpm.website.model.entity.PostEntities.QPost;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public class IgnorePostStateTypeBinding implements QuerydslBinderCustomizer<QPost> {

    @Override
    public void customize(QuerydslBindings bindings, QPost post) {
        bindings.excluding(post.postState);
    }
}
