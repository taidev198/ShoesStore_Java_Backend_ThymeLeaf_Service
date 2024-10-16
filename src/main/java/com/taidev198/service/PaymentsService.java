package com.taidev198.service;

import java.util.List;
import java.util.Map;

import com.taidev198.bean.PayForm;
import com.taidev198.bean.ShoppingCartInfo;
import com.taidev198.model.Account;

public interface PaymentsService {
    void processPayments(
            List<ShoppingCartInfo> shoppingCartInfos, PayForm payForm, Account currentAccount, Integer totalPrice);

    Map<String, ?> singlePayment(int quantityID, Account currrentAccount, int quantity);
}
