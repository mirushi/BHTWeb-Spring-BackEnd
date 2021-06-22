package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "exercise_topic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "exercises.all",
        attributeNodes = {
                @NamedAttributeNode(value = "exercises")
        }
)
public class ExerciseTopic {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_topic_sequence"
    )
    @SequenceGenerator(
            name = "exercise_topic_sequence",
            sequenceName = "exercise_topic_sequence"
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "rank")
    private Integer rank;

    @ManyToOne
    private ExerciseSubject subject;

    @OneToMany (
            mappedBy = "topic",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Exercise> exercises;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseTopic)) return false;
        ExerciseTopic other = (ExerciseTopic) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
