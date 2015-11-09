package com.simpleApplications.audioRecorder.model.validators;

import com.google.inject.Inject;
import com.simpleApplications.audioRecorder.daos.interfaces.IRecordingProjectDao;
import com.simpleApplications.audioRecorder.model.constraints.UniqueProjectNameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Nico Moehring
 */
public class UniqueProjectNameValidator implements ConstraintValidator<UniqueProjectNameConstraint, String> {

    protected IRecordingProjectDao dao;

    @Inject
    public UniqueProjectNameValidator(IRecordingProjectDao dao) {
        this.dao = dao;
    }

    @Override
    public void initialize(UniqueProjectNameConstraint uniqueProjectNameConstraint) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return this.dao.getByName(s) == null;
    }
}
