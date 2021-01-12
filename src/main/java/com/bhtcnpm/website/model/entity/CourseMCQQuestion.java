package com.bhtcnpm.website.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course_mcq_question")
@Data
public class CourseMCQQuestion {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "course_mcq_question_sequence"
    )
    @SequenceGenerator(
            name = "course_mcq_question_sequence",
            sequenceName = "course_mcq_question_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String explaination;

    @Column(nullable = false)
    private Long position;

    @ManyToOne (fetch = FetchType.LAZY)
    private CourseMCQHeading heading;

    @OneToMany (
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CourseMCQAnswer> answers;

    @Version
    private short version;
}
