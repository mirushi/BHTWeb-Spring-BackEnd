package com.bhtcnpm.website.model.entity.UserWebsiteEntities;

import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "reputation_score_definition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReputationScoreDefinition {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reputation_score_definition_sequence"
    )
    @SequenceGenerator(
            name = "reputation_score_definition_sequence",
            sequenceName = "reputation_score_definition_sequence"
    )
    private Long id;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private ReputationType reputationType;

    @Column(name = "score")
    private Integer score;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ReputationScoreDefinition)) return false;
        ReputationScoreDefinition other = (ReputationScoreDefinition) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
