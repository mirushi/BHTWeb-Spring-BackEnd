package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "exercise_answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseAnswer {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_answer_sequence"
    )
    @SequenceGenerator(
            name = "exercise_answer_sequence",
            sequenceName = "exercise_answer_sequence"
    )
    private Long id;

    @Lob
    @Column(name = "content", columnDefinition = "text",nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @Column(name = "rank", nullable = false)
    private Integer rank;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false ,updatable = false)
    private ExerciseQuestion question;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseQuestion)) return false;
        ExerciseQuestion other = (ExerciseQuestion) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
