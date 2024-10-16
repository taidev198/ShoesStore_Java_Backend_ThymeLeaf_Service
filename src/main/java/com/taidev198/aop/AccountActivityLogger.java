package com.taidev198.aop;


import com.taidev198.bean.LoginRequest;
import com.taidev198.model.Account;
import com.taidev198.model.AccountActivity;
import com.taidev198.model.Enum.AccountActivityEnum;
import com.taidev198.service.AccountActivityService;
import com.taidev198.service.AccountsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.Arrays;

@Slf4j
@Component
@Aspect

public class AccountActivityLogger {

    @Autowired
    private AccountActivityService accountActivityService;

    @Autowired
    private AccountsService accountsService;

    @Pointcut("execution(* com.taidev198.controller.LoginController.*(..))")
    public void onAccountActivityLogger() {

    }

    @AfterReturning("onAccountActivityLogger()")
    public Object afterAccountActivityLogger(JoinPoint joinPoint) {
        var result = joinPoint.getArgs()[0];
        LoginRequest loginRequest = null;
        if (result instanceof BindingAwareModelMap) {
           loginRequest =
               (LoginRequest)((BindingAwareModelMap)result).get("loginRequest");
        } else if (result instanceof MethodInvocationProceedingJoinPoint) {
            loginRequest =
                (LoginRequest)((MethodInvocationProceedingJoinPoint)result).getArgs()[0];
        } else if (result instanceof LoginRequest) {
            loginRequest = (LoginRequest)result;
        }
        if(loginRequest!= null && loginRequest.getEmail() != null) {
            var account = accountsService.findAccountByEmail(loginRequest.getEmail());
            accountActivityService.save(
                AccountActivity.builder()
                    .accountId(Long.valueOf(account.getId()))
                    .activity(AccountActivityEnum.LOGGED.name())
                    .build()
            );
        }
        return joinPoint.getArgs()[0];
    }
}
