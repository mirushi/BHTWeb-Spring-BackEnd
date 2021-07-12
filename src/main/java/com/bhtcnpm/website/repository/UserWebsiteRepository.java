package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.UserWebsite.UserFullStatisticDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserStatisticDTO;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserWebsiteRepository extends JpaRepository<UserWebsite, UUID> {
    Optional<UserWebsite> findByName (String name);
    Optional<UserWebsite> findByNameOrDisplayNameOrEmail (String name,String displayName, String email);

    @Query("SELECT new com.bhtcnpm.website.model.dto.UserWebsite.UserStatisticDTO(COUNT(DISTINCT uwPost.id), COUNT (DISTINCT uwDoc.id)) " +
            "FROM UserWebsite uw " +
            "LEFT JOIN uw.postedPosts uwPost " +
            "LEFT JOIN uw.postedDocs uwDoc " +
            "WHERE uw.id = :userID")
    UserStatisticDTO getUserWebsiteStatistic (UUID userID);

    @Query("SELECT new com.bhtcnpm.website.model.dto.UserWebsite.UserFullStatisticDTO(uw.reputationScore, COUNT(DISTINCT uwPost.id), COUNT(DISTINCT uwDoc.id)) " +
            "FROM UserWebsite uw " +
            "LEFT JOIN uw.postedPosts uwPost " +
            "LEFT JOIN uw.postedDocs uwDoc " +
            "WHERE uw.id = :userID")
    UserFullStatisticDTO getUserWebsiteFullStatistic (UUID userID);

    List<UserWebsite> findAllByNameOrDisplayNameOrEmail (String name, String displayName, String email);

    @Query("SELECT uw.name FROM UserWebsite uw WHERE uw.email = :email")
    String findUsernameByEmail (String email);

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE USER_WEBSITE uw SET uw.REPUTATION_SCORE = uw.REPUTATION_SCORE + " +
                    ":count * (SELECT rsd.SCORE FROM REPUTATION_SCORE_DEFINITION rsd WHERE rsd.reputation_type = :reputationTypeOrdinal) " +
                    "WHERE uw.id = :authorID ")
    int addUserReputationScore(UUID authorID, short reputationTypeOrdinal, long count);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE USER_WEBSITE uw SET uw.REPUTATION_SCORE = uw.REPUTATION_SCORE + " +
            "(-1) * :count * (SELECT rsd.SCORE FROM REPUTATION_SCORE_DEFINITION rsd WHERE rsd.reputation_type = :reputationTypeOrdinal) " +
            "WHERE uw.id = :authorID ")
    int subtractUserReputationScore(UUID authorID, short reputationTypeOrdinal, long count);
}
