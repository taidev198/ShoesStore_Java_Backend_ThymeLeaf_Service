package com.taidev198.annotation;

import java.lang.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Documented
@AuthenticationPrincipal
public @interface CurrentAccount {}
