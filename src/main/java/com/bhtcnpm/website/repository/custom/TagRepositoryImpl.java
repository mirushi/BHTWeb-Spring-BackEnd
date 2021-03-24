package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Tag.TagDTO;
import com.bhtcnpm.website.model.dto.Tag.TagMapper;
import com.bhtcnpm.website.model.entity.QTag;
import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.search.lucene.LuceneIndexUtils;
import org.apache.lucene.index.IndexReader;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.List;

@Component
public class TagRepositoryImpl implements TagRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final TagMapper tagMapper;

    private final SearchSession searchSession;

    private final IndexReader luceneIndexReader;

    private final QTag qTag = QTag.tag;

    public TagRepositoryImpl(EntityManager em, TagMapper tagMapper) throws IOException {
        this.em = em;
        this.tagMapper = tagMapper;

        this.luceneIndexReader = LuceneIndexUtils.getReader("Tag");
        this.searchSession = Search.session(em);
    }

    @Override
    public List<TagDTO> getRelatedTags(Tag entity, int limit) {
        SearchResult<Tag> searchResult = searchSession.search(Tag.class)
                .where(f -> f.bool(b -> {
                    b.filter(f.matchAll());
                    b.should(f.match().field("content").matching(entity.getContent()));
                })).fetch(limit);

        return tagMapper.tagListToTagDTOList(searchResult.hits());
    }
}
