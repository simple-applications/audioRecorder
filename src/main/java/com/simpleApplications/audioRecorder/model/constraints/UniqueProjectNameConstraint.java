package com.simpleApplications.audioRecorder.model.constraints;

import com.simpleApplications.audioRecorder.model.validators.UniqueProjectNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Nico Moehring
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueProjectNameValidator.class)
@Documented
public @interface UniqueProjectNameConstraint {
    String message() default "{com.simpleApplications.audioRecorder.model.constraints.uniqueProjectNameConstraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
