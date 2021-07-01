package com.bhtcnpm.website.model.entity;

import java.util.List;

//@Entity
//@Data
//@EqualsAndHashCode(callSuper = true)
public class CourseMCQHeading extends CourseContent{
//    @OneToMany (
//            mappedBy = "heading",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
    private List<CourseMCQQuestion> questions;
}
