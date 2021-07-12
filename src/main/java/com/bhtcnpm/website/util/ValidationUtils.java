package com.bhtcnpm.website.util;

public class ValidationUtils {
    public static void assertExactlyOneParamIsNotNull(Object... params) {
        final String multipleParamViolation = "Only one param is allowed at a time.";
        final String noParamViolation = "Exactly one param must not be null.";

        boolean isNotNullParamExist = false;
        for (int i=0; i<params.length; ++i) {
            Object currentParam = params[i];
            if (currentParam != null && isNotNullParamExist) {
                throw new IllegalArgumentException(multipleParamViolation);
            } else if (currentParam != null) {
                isNotNullParamExist = true;
            }
        }
        if (!isNotNullParamExist) {
            throw new IllegalArgumentException(noParamViolation);
        }
    }

    public static void assertAtMostOneParamIsTrue (boolean... params) {
        final String multipleParamViolation = "At most one param has true value is allowed at a time.";

        boolean isTrueParamExist = false;
        for (int i=0; i<params.length; ++i) {
            boolean currentParam = params[i];
            if (currentParam && isTrueParamExist) {
                throw new IllegalArgumentException(multipleParamViolation);
            } else if (currentParam) {
                isTrueParamExist = true;
            }
        }
    }
}
