package com.bhtcnpm.website.model.entity.PostEntities;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_post_like")
@Data
public class UserPostLike {
    @EmbeddedId
    private UserPostLikeId userPostLikeId;
}
