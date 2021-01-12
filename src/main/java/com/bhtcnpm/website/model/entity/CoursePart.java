package com.bhtcnpm.website.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "course_part")
@Data
public class CoursePart {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "course_part_sequence"
    )
    @SequenceGenerator(
            name = "course_part_sequence",
            sequenceName = "course_part_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private CourseHeading courseHeading;

    @Version
    private short version;

}
