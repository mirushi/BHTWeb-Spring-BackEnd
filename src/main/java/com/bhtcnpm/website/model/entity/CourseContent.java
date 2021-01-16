package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.model.entity.enumeration.CourseContentType.CourseContentType;
import com.bhtcnpm.website.model.entity.enumeration.CourseContentType.CourseContentTypeConverter;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "course_content")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class CourseContent {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "course_content_sequence"
    )
    @SequenceGenerator(
            name = "course_category_sequence",
            sequenceName = "course_content_sequence"
    )
    protected Long id;

    @Column(nullable = false)
    protected String name;

    @Enumerated
    @Column(columnDefinition = "smallint")
    protected CourseContentType type;

    @Lob
    @Column(nullable = false)
    protected String content;

    @Column(nullable = false)
    protected Long position;

    @Version
    protected short version;
}
