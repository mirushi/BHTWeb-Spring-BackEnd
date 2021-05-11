package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.UserWebsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//TODO: Consider removing hashedPassword from retrieval.
public interface UserWebsiteRepository extends JpaRepository<UserWebsite, Long> {
    Optional<UserWebsite> findByName (String name);
    Optional<UserWebsite> findByNameOrDisplayNameOrEmail (String name,String displayName, String email);

    List<UserWebsite> findAllByNameOrDisplayNameOrEmail (String name, String displayName, String email);

    @Query("SELECT uw.name FROM UserWebsite uw WHERE uw.email = :email")
    String findUsernameByEmail (String email);
}
