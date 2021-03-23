package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.entity.Tag;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;

import javax.naming.directory.SearchResult;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class TagRepositoryImpl implements TagRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final SearchSession searchSession;

    public TagRepositoryImpl(EntityManager em) {
        this.em = em;
        this.searchSession = Search.session(em);
    }

    @Override
    public List<TagDTO> getRelatedTags(Tag entity, int limit) {
        SearchResult<Tag> searchResult = searchSession.search(Tag.class)
                .where(f -> )

        return null;
    }
}
