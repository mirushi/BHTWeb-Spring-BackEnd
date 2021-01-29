package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Post.PostQuickSearchResult;
import com.bhtcnpm.website.model.dto.Post.PostStatisticDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryDTO;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.repository.custom.PostRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, QuerydslPredicateExecutor<Post>, JpaSpecificationExecutor<Post>, PostRepositoryCustom {
    @Query("SELECT new com.bhtcnpm.website.model.dto.Post.PostStatisticDTO(p.id, COUNT(DISTINCT pc.id) ,COUNT(DISTINCT uLiked.userPostLikeId.user.id), " +
            "CASE WHEN EXISTS (SELECT 1 FROM p.userPostLikes uLikedSub WHERE uLikedSub.userPostLikeId.user.id = :userID) THEN true ELSE false END, " +
            "CASE WHEN EXISTS (SELECT 1 FROM p.userPostSaves uSavedSub WHERE uSavedSub.userPostSaveId.user.id = :userID) THEN true ELSE false END) " +
            "FROM Post p " +
            "LEFT JOIN p.userPostLikes uLiked " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "WHERE p.id IN :postIDs " +
            "GROUP BY p.id")
    List<PostStatisticDTO> getPostStatisticDTOs (List<Long> postIDs, Long userID);

    @Modifying
    @Query("UPDATE Post as p " +
            "SET p.postState = :postStateType " +
            "WHERE p.id = :postID")
    int setPostState (Long postID, PostStateType postStateType);

    List<Post> findByCategoryNameOrderByPublishDtmDesc (Pageable pageable, String categoryName);

    //Search Term is the same as Search Term Exact. Please don't pass different value.
    @Query ("SELECT new com.bhtcnpm.website.model.dto.Post.PostQuickSearchResult(p.id, p.imageURL, p.title) " +
            "FROM Post p " +
            "WHERE p.title LIKE %:searchTerm% " +
            "ORDER BY "+ "(CASE WHEN EXISTS (SELECT 1 FROM p WHERE p.title = :searchTermExact) THEN TRUE ELSE FALSE END)" +" DESC, length(p.title)")
    List<PostQuickSearchResult> quickSearch (Pageable pageable, String searchTerm, String searchTermExact);

//    @Query (value = "SELECT new com.bhtcnpm.website.model.dto.Post.PostSummaryDTO(p.id, p.author.id, p.author.name, p.category.id, p.category.name, p.imageURL, p.publishDtm, p.readingTime, p.summary, p.title) " +
//            "FROM Post p " +
//            "WHERE p.title LIKE %:searchTerm% " +
//            "ORDER BY "+ "(CASE WHEN EXISTS (SELECT 1 FROM p WHERE p.title = :searchTermExact) THEN TRUE ELSE FALSE END) ")
//    Page<PostSummaryDTO> searchBySearchTerm (Specification specification, Pageable pageable, String searchTerm, String searchTermExact);
}
