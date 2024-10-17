package com.taidev198.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taidev198.service.AccountActivityService;
import com.taidev198.service.AccountsService;
import com.taidev198.util.exception.PermisticLockingFailureException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class AccountActivityLogger {

    private static final int DEFAULT_MAX_RETRIES = 2;

    private int maxRetries = DEFAULT_MAX_RETRIES;

    @Autowired
    private AccountActivityService accountActivityService;

    @Autowired
    private AccountsService accountsService;

    @Pointcut("execution(* com.taidev198.controller.LoginController.*(..))")
    public void onAccountActivityLogger() {}

    @AfterReturning("onAccountActivityLogger()")
    public Object afterAccountActivityLogger(JoinPoint joinPoint) {
        //        var result = joinPoint.getArgs()[0];
        //        LoginRequest loginRequest = null;
        //        if (result instanceof BindingAwareModelMap) {
        //           loginRequest =
        //               (LoginRequest)((BindingAwareModelMap)result).get("loginRequest");
        //        } else if (result instanceof MethodInvocationProceedingJoinPoint) {
        //            loginRequest =
        //                (LoginRequest)((MethodInvocationProceedingJoinPoint)result).getArgs()[0];
        //        } else if (result instanceof LoginRequest) {
        //            loginRequest = (LoginRequest)result;
        //        }
        //        if(loginRequest!= null && loginRequest.getEmail() != null) {
        //            var account = accountsService.findAccountByEmail(loginRequest.getEmail());
        //            accountActivityService.save(
        //                AccountActivity.builder()
        //                    .accountId(Long.valueOf(account.getId()))
        //                    .activity(AccountActivityEnum.LOGGED.name())
        //                    .build()
        //            );
        //        }
        return joinPoint.getArgs()[0];
    }

    @Around("com.taidev198.CommonPointcuts.businessService()")
    public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
        int numAttempts = 0;
        PermisticLockingFailureException lockFailureException;
        do {
            numAttempts++;
            try {
                return pjp.proceed();
            } catch (PermisticLockingFailureException ex) {
                lockFailureException = ex;
            }
        } while (numAttempts <= this.maxRetries);
        throw lockFailureException;
    }
}
