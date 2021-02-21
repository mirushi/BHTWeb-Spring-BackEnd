package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.UserWebsiteRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserWebsiteRoleRepository extends JpaRepository<UserWebsiteRole, Long> {
    @Query("select usr.id from UserWebsiteRole usr where usr.name = :name")
    Long findIdByName (String name);
}
