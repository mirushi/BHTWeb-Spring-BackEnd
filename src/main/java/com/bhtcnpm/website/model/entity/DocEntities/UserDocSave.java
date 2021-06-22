package com.bhtcnpm.website.model.entity.DocEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_doc_save")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDocSave {
    @EmbeddedId
    private UserDocSaveId userDocSaveId;
}
