package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.UserWebsiteEntities.ReputationScoreDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReputationScoreDefinitionRepository extends JpaRepository<ReputationScoreDefinition, Long> {
}
