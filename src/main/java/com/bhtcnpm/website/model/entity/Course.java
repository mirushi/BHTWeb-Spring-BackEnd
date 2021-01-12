package com.bhtcnpm.website.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "course")
@Data
public class Course {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "course_sequence"
    )
    @SequenceGenerator(
            name = "course_sequence",
            sequenceName = "course_sequence",
            allocationSize = 3
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    private CourseSubjectGroup subjectGroup;

    @ManyToOne
    private CourseCategory category;

    @ManyToMany (cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable (
            name = "course_user_saved",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserWebsite> usersSaved;

    @OneToMany (
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CourseHeading> courseHeadings;

    @Version
    private short version;

}
