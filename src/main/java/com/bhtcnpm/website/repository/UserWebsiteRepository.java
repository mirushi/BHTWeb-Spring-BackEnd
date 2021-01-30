package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.UserWebsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWebsiteRepository extends JpaRepository<UserWebsite, Long> {
}
