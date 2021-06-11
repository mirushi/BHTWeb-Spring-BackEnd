package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.UserWebsite.UserStatisticDTO;
import com.bhtcnpm.website.model.entity.UserWebsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
//TODO: Consider removing hashedPassword from retrieval.
public interface UserWebsiteRepository extends JpaRepository<UserWebsite, UUID> {
    Optional<UserWebsite> findByName (String name);
    Optional<UserWebsite> findByNameOrDisplayNameOrEmail (String name,String displayName, String email);

    @Query("SELECT new com.bhtcnpm.website.model.dto.UserWebsite.UserStatisticDTO(COUNT(DISTINCT uwPost.id), COUNT (DISTINCT uwDoc.id)) " +
            "FROM UserWebsite uw " +
            "LEFT JOIN uw.postedPosts uwPost " +
            "LEFT JOIN uw.postedDocs uwDoc " +
            "WHERE uw.id = :userID")
    UserStatisticDTO getUserWebsiteStatistic (UUID userID);

    List<UserWebsite> findAllByNameOrDisplayNameOrEmail (String name, String displayName, String email);

    @Query("SELECT uw.name FROM UserWebsite uw WHERE uw.email = :email")
    String findUsernameByEmail (String email);
}
