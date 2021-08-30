package com.example.mom.home.assignment.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SpouseValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateSpouseAnnotation {
    String message() default "Please provide a spouse if you are not single or divorced";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
