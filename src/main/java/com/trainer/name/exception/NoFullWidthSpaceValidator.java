package com.trainer.name.exception;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

public class NoFullWidthSpaceValidator implements ConstraintValidator<NoFullWidthSpaceValidator.NoFullWidthSpace, String> {
    @Override
    public void initialize(NoFullWidthSpace constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.matches(".*[\\s　]+.*");
    }

    @Documented
    @Constraint(validatedBy = NoFullWidthSpaceValidator.class)
    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NoFullWidthSpace {
        String message() default "{NoFullWidthSpace.message}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}
