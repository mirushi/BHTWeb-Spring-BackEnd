package com.bhtcnpm.website.repository.Post;

import com.bhtcnpm.website.model.entity.PostEntities.UserPostSave;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostSaveId;
import com.bhtcnpm.website.repository.custom.UserPostSaveRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostSaveRepository extends JpaRepository<UserPostSave, UserPostSaveId>, UserPostSaveRepositoryCustom {
}
