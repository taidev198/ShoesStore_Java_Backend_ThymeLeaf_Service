package com.taidev198.service.impl;

import java.util.*;

import com.taidev198.service.ChatMessageService;
import com.taidev198.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.taidev198.model.Account;
import com.taidev198.model.Enum.AccountRole;
import com.taidev198.repository.AccountRepository;
import com.taidev198.service.AccountsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountsServiceImpl implements AccountsService {

    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatMessageService chatMessageService;

    public Page<Account> findAccountsByFilter(
            int page, int size, String order, String role, String sortBy, String query) {
        Pageable pageable = PageRequest.of(
                page - 1, size, order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);

        List<AccountRole> roles;
        if ("customer".equalsIgnoreCase(role)) {
            roles = Collections.singletonList(AccountRole.CUSTOMER);
        } else if ("manager".equalsIgnoreCase(role)) {
            roles = Arrays.asList(AccountRole.ADMIN, AccountRole.SELLER);
        } else {
            roles = Arrays.asList(AccountRole.values());
        }

        return accountRepository.findByFilter(query, roles, pageable);
    }

    @Override
    public void toggleAccountActivation(int accountId, boolean active) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            account.setIsActivated(active);
            accountRepository.save(account);
        }
    }

    @Override
    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<Optional<Account>> findAllById(Integer senderId) {
        List<String> receiverId = chatRoomService.findReceiverId(String.valueOf(senderId));
        var list = new ArrayList<>(receiverId.
            stream().map(r -> accountRepository.findAccountById(Integer.valueOf(r))).toList());
        return list.stream().sorted(
            ((o1, o2) -> {
                var chatMessage = chatMessageService.findChatMessages(String.valueOf(senderId), o1.get().getUsername());
                var chatMessage1 = chatMessageService.findChatMessages(String.valueOf(senderId), o2.get().getUsername());
                return chatMessage1.get(chatMessage1.size() -1).getTimestamp().after(chatMessage.get(chatMessage.size() -1).getTimestamp()) ? 1 : -1;
            })).toList();
    }

    @Override
    public Account save(Account account) {
       return accountRepository.save(account);
    }
}
