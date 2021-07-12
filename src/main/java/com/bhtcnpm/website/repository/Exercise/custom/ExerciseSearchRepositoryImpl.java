package com.bhtcnpm.website.repository.Exercise.custom;

import com.bhtcnpm.website.constant.business.Exercise.ExerciseSearchConstant;
import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Exercise.filter.ExerciseSearchFilterRequestDTO;
import com.bhtcnpm.website.model.dto.Exercise.sort.ExerciseSearchSortRequestDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseCategory;
import com.bhtcnpm.website.model.entity.SubjectEntities.Subject;
import com.bhtcnpm.website.model.entity.UserWebsite;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.SearchSort;
import org.hibernate.search.engine.search.sort.dsl.SortThenStep;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Component
public class ExerciseSearchRepositoryImpl implements ExerciseSearchRepository {

    @PersistenceContext
    private final EntityManager em;

    private final SearchSession searchSession;

    public ExerciseSearchRepositoryImpl(EntityManager em) {
        this.em = em;
        this.searchSession = Search.session(em);
    }

    @Override
    public Page<Exercise> searchPublicExercise(ExerciseSearchFilterRequestDTO filterRequestDTO, ExerciseSearchSortRequestDTO sortRequestDTO, Integer page, Integer pageSize, Authentication authentication) {
        SearchResult<Exercise> exerciseSearchResult = getExerciseSearchResult(filterRequestDTO, sortRequestDTO, page, pageSize, authentication);

        return this.searchResultToPage(exerciseSearchResult, page, pageSize);
    }

    private SearchResult<Exercise> getExerciseSearchResult (ExerciseSearchFilterRequestDTO filterRequestDTO,
                                                            ExerciseSearchSortRequestDTO sortRequestDTO,
                                                            Integer page,
                                                            Integer pageSize,
                                                            Authentication authentication) {
        SearchScope<Exercise> scope = getSearchScope();

        //TODO: Filter search result based on user reading permission.
        //TODO: Filter search result based on ExerciseBusinessState.

        final boolean userCustomSort = sortRequestDTO != null && (sortRequestDTO.getSortByPublishDtm() != null || sortRequestDTO.getSortByAttempts() != null);

        //Build dynamic sorting condition based on user input.
        //Intermediate step.
        SortThenStep sortThenStep = scope.sort().score();

        //Avoid null pointer exception - inverse equals caller and callee.
        if (sortRequestDTO != null) {
            if (ApiSortOrder.ASC.equals(sortRequestDTO.getSortByPublishDtm())) {
                sortThenStep = sortThenStep.then().field("publishDtm").asc();
            } else if (ApiSortOrder.DESC.equals(sortRequestDTO.getSortByPublishDtm())) {
                sortThenStep = sortThenStep.then().field("publishDtm").desc();
            }
        }
        SearchSort searchSort = sortThenStep.toSort();
        //TODO: Do a sort for attempts count of user.
        SearchResult<Exercise> searchResult = searchSession.search(Exercise.class)
                .where(f -> f.bool(b -> {
                    b.filter(f.matchAll());
                    if (filterRequestDTO != null) {
                        String searchTerm = filterRequestDTO.getSearchTerm();
                        Long categoryID = filterRequestDTO.getCategoryID();
                        Long subjectID = filterRequestDTO.getSubjectID();
                        UUID authorID = filterRequestDTO.getAuthorID();
                        Long tagID = filterRequestDTO.getTags();
                        if (StringUtils.isNotEmpty(searchTerm)) {
                            var predicate = f.match()
                                    .field("title")
                                    .boost(ExerciseSearchConstant.SEARCH_TITLE_BOOST)
                                    .field("description")
                                    .boost(ExerciseSearchConstant.SEARCH_DESCRIPTION_BOOST)
                                    .matching(filterRequestDTO.getSearchTerm());
                            if (userCustomSort) {
                                b.filter(predicate);
                            } else {
                                b.must(predicate);
                            }
                        }
                        if (categoryID != null) {
                            b.filter(f.match()
                                    .field("categoryID")
                                    .matching(em.getReference(ExerciseCategory.class, categoryID)));
                        }
                        if (subjectID != null) {
                            b.filter(f.match()
                                    .field("subjectID")
                                    .matching(em.getReference(Subject.class, subjectID)));
                        }
                        if (authorID != null) {
                            b.filter(f.match()
                                    .field("authorID")
                                    .matching(em.getReference(UserWebsite.class, authorID)));
                        }
                        if (tagID != null) {
                            b.filter(f.match()
                                    .field("tags_eb.id")
                                    .matching(tagID));
                        }
                    }
                })).sort(searchSort)
                .fetch(page * pageSize, pageSize);

        return searchResult;
    }

    private SearchScope<Exercise> getSearchScope () { return searchSession.scope(Exercise.class); }

    private Page<Exercise> searchResultToPage (SearchResult<Exercise> exerciseSearchResult, int page, int pageSize) {
        long resultCount = exerciseSearchResult.total().hitCountLowerBound();
        int totalPages = (int)Math.ceil((double)resultCount/pageSize);
        List<Exercise> exerciseList = exerciseSearchResult.hits();
        return new PageImpl<>(exerciseList, PageRequest.of(page, pageSize), resultCount);
    }
}
