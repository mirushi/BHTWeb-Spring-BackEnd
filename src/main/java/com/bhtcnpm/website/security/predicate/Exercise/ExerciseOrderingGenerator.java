package com.bhtcnpm.website.security.predicate.Exercise;

import org.springframework.data.domain.Sort;

public class ExerciseOrderingGenerator {
    public static Sort orderByRankAsc () {
        return Sort.by(Sort.Direction.ASC, "rank");
    }
}
