package com.bhtcnpm.website.model.entity;

import java.util.List;

//@Entity
//@Table(name = "course_subject_group")
//@Data
public class CourseSubjectGroup {

//    @Id
//    @GeneratedValue (
//            strategy = GenerationType.SEQUENCE,
//            generator = "course_subject_group_sequence"
//    )
//    @SequenceGenerator(
//            name = "course_subject_group_sequence",
//            sequenceName = "course_subject_group_sequence"
//    )
    private Long id;

//    @Column(nullable = false)
    private String name;

//    @OneToMany (
//            mappedBy = "subjectGroup",
//            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
//    )
    private List<Course> courses;

//    @Version
    private short version;

}
