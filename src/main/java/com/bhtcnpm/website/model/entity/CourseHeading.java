package com.bhtcnpm.website.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course_heading")
@Data
public class CourseHeading {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "course_heading_sequence"
    )
    @SequenceGenerator(
            name = "course_heading_sequence",
            sequenceName = "course_heading_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false)
    private Boolean savedStatus;

    @Column(nullable = false)
    private String description;

    @ManyToOne (fetch = FetchType.LAZY)
    private Course course;

    @OneToMany (
            mappedBy = "courseHeading",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CoursePart> courseParts;

    @Version
    private short version;
}
