package com.bhtcnpm.website.repository.custom;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
public class UserDocReactionRepositoryImpl implements UserDocReactionRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;


}
