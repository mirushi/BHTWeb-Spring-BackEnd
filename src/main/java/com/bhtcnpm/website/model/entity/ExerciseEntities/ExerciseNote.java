package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "exercise_note")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseNote {
    @EmbeddedId
    private ExerciseNoteId id;

    @Column(name = "notes")
    private String notes;

    @Version
    private short version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseNote)) return false;
        ExerciseNote other = (ExerciseNote) o;

        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {return Objects.hash(getId());}
}
