package com.bhtcnpm.website.model.entity.ExerciseEntities;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseQuestion.ExerciseQuestionStateType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercise_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "answers.all",
        attributeNodes = {
                @NamedAttributeNode(value = "answers")
        }
)
@NamedEntityGraph(
        name = "answers.correct",
        attributeNodes = {
                @NamedAttributeNode(value = "correctAnswers")
        }
)
@Builder
public class ExerciseQuestion {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_question_sequence"
    )
    @SequenceGenerator(
            name = "exercise_question_sequence",
            sequenceName = "exercise_question_sequence"
    )
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "explanation")
    private String explanation;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserWebsite author;

    @Column(name = "suggested_duration")
    private Integer suggestedDuration;

    @Column(name = "submit_dtm", updatable = false)
    @Builder.Default
    private LocalDateTime submitDtm = LocalDateTime.now();

    @Column(name = "publish_dtm")
    @Builder.Default
    private LocalDateTime publishDtm = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private UserWebsite lastUpdatedBy;

    @Column
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDtm = LocalDateTime.now();

    @Column(name = "state_type", nullable = false)
    private ExerciseQuestionStateType stateType;

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ExerciseAnswer> answers;

    @OneToMany(
            mappedBy = "question"
    )
    @Where(clause = "is_correct = true")
    private List<ExerciseAnswer> correctAnswers;

    @ManyToOne
    @JoinColumn(name = "difficulty_type_id")
    private ExerciseQuestionDifficulty difficultyType;

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

    public void setAnswers (List<ExerciseAnswer> answers) {
        if (this.answers == null) {
            this.answers = new ArrayList<>();
        }
        this.answers.clear();
        if (answers != null) {
            this.answers.addAll(answers);
        }
    }
}
