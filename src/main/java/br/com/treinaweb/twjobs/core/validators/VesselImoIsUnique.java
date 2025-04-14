package br.com.treinaweb.twjobs.core.validators;

import br.com.treinaweb.twjobs.core.models.Vessel;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VesselImoisUniqueValidator.class)
public @interface VesselImoIsUnique {

    String message() default "this ${validatedValue} vessel Imo is already in use";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };


}
