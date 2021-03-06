package com.store.validation.validator;

import com.store.payload.request.SignUpRequest;
import com.store.validation.constraint.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        SignUpRequest user = (SignUpRequest) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
