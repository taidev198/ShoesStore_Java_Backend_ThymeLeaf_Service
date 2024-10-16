package com.taidev198.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.taidev198.bean.ShoppingCartInfo;
import com.taidev198.model.Account;
import com.taidev198.model.ProductQuantity;

public interface ShoppingCartsService {
    List<ShoppingCartInfo> getShoppingCartsByCustomerId(Integer customerId);

    Page<ShoppingCartInfo> getShoppingCartsByCustomerId(Integer customerId, Pageable pageable);

    String updateProductInCart(int cartId, int accountId, int quantityId, int quantity, String action);

    void addProductToCart(Account account, ProductQuantity productQuantity, int quantity);

    @Transactional
    void deleteCartItemByID(int cartId, int accountId);

    @Transactional
    void deleteAllCartItemsByAccountId(int accountId);
}
