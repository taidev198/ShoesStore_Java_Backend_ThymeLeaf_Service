package com.taidev198.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.taidev198.util.validation.PasswordsMatchValidator;

@Constraint(validatedBy = PasswordsMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordsMatch {
    String message() default "Mật khẩu nhập lại không khớp";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
