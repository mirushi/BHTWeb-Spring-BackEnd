package com.bhtcnpm.website.model.entity.ExerciseEntities;

import com.bhtcnpm.website.model.entity.Tag;
import com.bhtcnpm.website.model.entity.UserWebsite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "exercise")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_sequence"
    )
    @SequenceGenerator(
            name = "exercise_sequence",
            sequenceName = "exercise_sequence"
    )
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "suggested_duration")
    private Integer suggestedDuration;

    @Column(name = "submit_dtm", nullable = false, updatable = false)
    private LocalDateTime submitDtm;

    @Column(name = "publish_dtm", nullable = false, updatable = false)
    private LocalDateTime publishDtm;

    @ManyToOne
    private UserWebsite author;

    @ManyToOne
    private ExerciseTopic topic;

    @ManyToOne
    private ExerciseCategory category;

    @ManyToMany(cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE})
    @JoinTable(
            name = "exercise_exercise_tag",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise)) return false;
        Exercise other = (Exercise) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
