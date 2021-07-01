package com.bhtcnpm.website.model.entity;

//@Entity
//@Table(name = "course_mcq_answer")
//@Data
public class CourseMCQAnswer {

//    @Id
//    @GeneratedValue (
//            strategy = GenerationType.SEQUENCE,
//            generator = "course_mcq_answer_sequence"
//    )
//    @SequenceGenerator(
//            name = "course_mcq_answer_sequence",
//            sequenceName = "course_mcq_answer_sequence"
//    )
    private Long id;

//    @Lob
//    @Column(nullable = false)
    private String content;

//    @Column(nullable = false)
    private Boolean isCorrect;

//    @Column(nullable = false)
    private Long position;

//    @ManyToOne(fetch = FetchType.LAZY)
    private CourseMCQQuestion question;

//    @Version
    private short version;
}
