package com.taidev198.util.validation;

import com.taidev198.annotation.PasswordsMatch;
import com.taidev198.bean.AccountRegistration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, AccountRegistration> {

    @Override
    public boolean isValid(AccountRegistration accountRegistration, ConstraintValidatorContext context) {
        if (accountRegistration.getPassword() == null || accountRegistration.getConfirmPassword() == null) {
            return false;
        }

        return accountRegistration.getPassword().equals(accountRegistration.getConfirmPassword());
    }
}

