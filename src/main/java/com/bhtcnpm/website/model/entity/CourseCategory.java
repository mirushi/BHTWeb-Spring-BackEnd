package com.bhtcnpm.website.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course_category")
@Data
public class CourseCategory {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "course_category_sequence"
    )
    @SequenceGenerator(
            name = "course_category_sequence",
            sequenceName = "course_category_sequence",
            allocationSize = 3
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany (
            mappedBy = "category",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    private List<Course> courses;

    @Version
    private short version;

}
