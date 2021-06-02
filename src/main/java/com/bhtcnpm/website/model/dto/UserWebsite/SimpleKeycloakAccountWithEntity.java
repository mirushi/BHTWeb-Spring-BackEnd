package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.model.entity.UserWebsite;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;

public class SimpleKeycloakAccountWithEntity extends SimpleKeycloakAccount {

    private UserWebsite entity;

    public SimpleKeycloakAccountWithEntity(SimpleKeycloakAccount keycloakAccount, UserWebsite entity) {
        super(keycloakAccount.getPrincipal(), keycloakAccount.getRoles(), keycloakAccount.getKeycloakSecurityContext());
        this.entity = entity;
    }

    public UserWebsite getEntity() {
        return this.entity;
    }
}
