package com.taidev198.controller.api;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taidev198.annotation.CurrentAccount;
import com.taidev198.bean.CartAPIRequest;
import com.taidev198.bean.CartAPIResponse;
import com.taidev198.bean.ShoppingCartInfo;
import com.taidev198.model.Account;
import com.taidev198.service.ShoppingCartsService;
import com.taidev198.util.exception.UnauthorizedException;

@RestController
@RequestMapping("/carts")
public class ShoppingCartsAPIController {
    private final ShoppingCartsService shoppingCartsService;

    @Autowired
    public ShoppingCartsAPIController(ShoppingCartsService shoppingCartsService) {
        this.shoppingCartsService = shoppingCartsService;
    }

    @PatchMapping(value = "/{id}/edit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CartAPIResponse> update(
            @PathVariable Integer id, @RequestBody CartAPIRequest requestBody, @CurrentAccount Account account) {
        if (account == null) {
            throw new UnauthorizedException("You need to login to update cart");
        }

        if (requestBody.getQuantity() == 0 || requestBody.getQuantity() < 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }

        if (requestBody.getProductQuantityId() == null || requestBody.getProductQuantityId() < 0) {
            throw new IllegalArgumentException("product quantity id must be greater than 0");
        }

        // "update" -> reload the page to not display duplicate product
        // "no-update" -> update UI on front-end
        String isNeedUpdate = shoppingCartsService.updateProductInCart(
                id,
                account.getId(),
                requestBody.getProductQuantityId(),
                requestBody.getQuantity(),
                requestBody.getAction());
        CartAPIResponse response = new CartAPIResponse();

        // recalculate total price if no reload the page
        if (isNeedUpdate.equals("no-update")) {
            List<ShoppingCartInfo> shoppingCarts = shoppingCartsService.getShoppingCartsByCustomerId(account.getId());
            int totalOriginPrice = 0;
            int finalPrice = 0;
            int totalDiscountedPrice = 0;

            for (ShoppingCartInfo shoppingCart : shoppingCarts) {
                totalOriginPrice += shoppingCart.getPrice();
                finalPrice += shoppingCart.getPrice() * (100 - shoppingCart.getDiscount()) / 100;
                totalDiscountedPrice += shoppingCart.getPrice() * shoppingCart.getDiscount() / 100;
            }

            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            String totalOriginPriceFormatted = numberFormat.format(totalOriginPrice) + " VND";
            String totalDiscountedPriceFormatted = numberFormat.format(totalDiscountedPrice) + " VND";
            String finalPriceFormatted = numberFormat.format(finalPrice) + " VND";
            response.setTotalOriginPrice(totalOriginPriceFormatted);
            response.setTotalDiscountedPrice(totalDiscountedPriceFormatted);
            response.setFinalPrice(finalPriceFormatted);
        }

        response.setMessage("Cập nhật giỏ hàng thành công !");
        response.setReload(isNeedUpdate.equals("update"));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
