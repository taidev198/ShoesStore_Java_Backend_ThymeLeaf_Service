package com.taidev198.util.aspect;

import com.taidev198.model.Account;
import com.taidev198.model.Enum.AccountRole;
import com.taidev198.util.exception.UnauthorizedException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AdminAuthorizationAspect {
    @Before("execution(* com.taidev198.controller.admin.AccountsController.*(..)) && args(account,..)")
    public void checkAdminAuthorization(Account account) {
        AccountRole role = account.getRole();
        if (role != AccountRole.ADMIN) {
            throw new UnauthorizedException("You are not authorized to access this page");
        }
    }
}
