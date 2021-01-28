package com.bhtcnpm.website.model.entity.PostEntities;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_post_save")
@Data
public class UserPostSave {
    @EmbeddedId
    private UserPostSaveId userPostSaveId;
}
