package com.bhtcnpm.website.model.entity.DocEntities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "doc_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocCategory {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "doc_category_sequence"
    )
    @SequenceGenerator(
                name = "doc_category_sequence",
            sequenceName = "doc_category_sequence",
            allocationSize = 3
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany (
            mappedBy = "category",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    private Set<Doc> docs;

    @Version
    private short version;

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof DocCategory)) return false;
        DocCategory other = (DocCategory) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode () {return getClass().hashCode();}
}
