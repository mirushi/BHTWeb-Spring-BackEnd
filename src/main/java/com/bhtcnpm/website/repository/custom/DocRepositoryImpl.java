package com.bhtcnpm.website.repository.custom;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
public class DocRepositoryImpl implements DocRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

}
