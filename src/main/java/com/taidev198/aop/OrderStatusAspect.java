package com.taidev198.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taidev198.service.MailService;

@Component
@Aspect
public class OrderStatusAspect {

    @Autowired
    private MailService mailService;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void onOrderStatusChanged() {}

    @Around("onOrderStatusChanged()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        // after method proceed

        return result;
    }
}
