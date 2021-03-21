package com.bhtcnpm.website;

import lombok.SneakyThrows;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.hibernate.search.engine.cfg.IndexSettings;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class HibernateSearchEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @PersistenceContext
    private EntityManager em;

    @SneakyThrows
    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        SearchSession searchSession = Search.session(em);
        searchSession.massIndexer().startAndWait();
    }

    @Autowired
    public void setEm (EntityManager em) {
        this.em = em;
    }
}
