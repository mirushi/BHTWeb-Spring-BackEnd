package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Post.PostSummaryDTO;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostSave;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostSaveId;
import com.bhtcnpm.website.repository.custom.UserPostSaveRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPostSaveRepository extends JpaRepository<UserPostSave, UserPostSaveId>, UserPostSaveRepositoryCustom {
}
