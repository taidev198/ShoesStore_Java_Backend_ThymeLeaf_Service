package com.taidev198.controller.common;

import com.taidev198.model.Account;
import com.taidev198.service.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final AccountsService accountsService;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public Account addClient(@Payload Account client) {
        return accountsService.save(client);
    }
}
