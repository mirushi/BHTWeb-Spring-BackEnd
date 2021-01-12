package com.bhtcnpm.website.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseMCQHeading extends CourseContent{

    @OneToMany (
            mappedBy = "heading",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CourseMCQQuestion> questions;
}
