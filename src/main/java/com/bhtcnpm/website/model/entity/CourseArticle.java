package com.bhtcnpm.website.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseArticle extends CourseContent {
    @Lob
    @Column(nullable = false)
    private String content;
}
