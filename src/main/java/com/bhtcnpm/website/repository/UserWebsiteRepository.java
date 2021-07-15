package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.UserWebsite.UserFullStatisticDTO;
import com.bhtcnpm.website.model.dto.UserWebsite.UserStatisticDTO;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import org.hibernate.jpa.TypedParameterValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
            "WHERE uw.id = :userID " +
            "GROUP BY uw ")
    UserFullStatisticDTO getUserWebsiteFullStatistic (UUID userID);

    List<UserWebsite> findAllByNameOrDisplayNameOrEmail (String name, String displayName, String email);

    @Query("SELECT uw.name FROM UserWebsite uw WHERE uw.email = :email")
    String findUsernameByEmail (String email);

    //Checked ok.
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE USER_WEBSITE SET REPUTATION_SCORE = REPUTATION_SCORE + " +
                    ":count * (SELECT rsd.SCORE FROM REPUTATION_SCORE_DEFINITION rsd WHERE rsd.reputation_type = :reputationTypeOrdinal) " +
                    "WHERE id = :authorID ")
    int addUserReputationScore(@Param("authorID") TypedParameterValue authorID, short reputationTypeOrdinal, long count);

    //Checked ok.
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE USER_WEBSITE SET REPUTATION_SCORE = REPUTATION_SCORE + " +
            "(-1) * :count * (SELECT rsd.SCORE FROM REPUTATION_SCORE_DEFINITION rsd WHERE rsd.reputation_type = :reputationTypeOrdinal) " +
            "WHERE id = :authorID ")
    int subtractUserReputationScore(@Param("authorID") TypedParameterValue authorID, short reputationTypeOrdinal, long count);
}
