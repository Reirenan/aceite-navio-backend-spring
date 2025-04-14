package br.com.treinaweb.twjobs.core.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BercoNameIsUniqueValidator.class)
public @interface BercoNameIsUnique {



    String message() default "this ${validatedValue} berco name is already in use";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
