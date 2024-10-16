package com.taidev198.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.taidev198.bean.PayForm;
import com.taidev198.bean.ShoppingCartInfo;
import com.taidev198.bean.ShoppingCartWrapper;
import com.taidev198.model.Account;
import com.taidev198.model.Enum.OrderStatus;
import com.taidev198.model.Order;
import com.taidev198.model.OrderDetail;
import com.taidev198.model.ProductQuantity;
import com.taidev198.repository.OrderDetailsRepository;
import com.taidev198.repository.OrdersRepository;
import com.taidev198.repository.ProductQuantitiesRepository;
import com.taidev198.repository.ShoppingCartsRepository;
import com.taidev198.service.PaymentsService;
import com.taidev198.util.constant.CommonConstant;
import com.taidev198.util.exception.BadRequestException;
import com.taidev198.util.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService {
    private final ShoppingCartsRepository shoppingCartRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductQuantitiesRepository productQuantitiesRepository;

    @Override
    public void processPayments(
            List<ShoppingCartInfo> shoppingCartInfos, PayForm payForm, Account currentAccount, Integer totalPrice) {
        try {
            if (CommonUtils.isEmptyOrNullList(shoppingCartInfos))
                throw new BadRequestException("Không có sản phẩm trong giỏ hàng");

            // Query 1: save order
            var order = ordersRepository.save(Order.builder()
                    .account(currentAccount)
                    .totalPrice(totalPrice)
                    .phoneNumber(payForm.getPhoneNumber())
                    .address(payForm.getAddress() + ", " + payForm.getProvince() + ", " + payForm.getDistrict() + ", "
                            + payForm.getWard())
                    .status(OrderStatus.WAIT)
                    .build());

            for (ShoppingCartInfo cartInfo : shoppingCartInfos) {
                ProductQuantity productQuantity = productQuantitiesRepository
                        .findById(cartInfo.getProductQuantityId())
                        .orElseThrow(() -> new BadRequestException("Sản phẩm không tồn tại"));

                // Check quantity
                if (productQuantity.getQuantity() < cartInfo.getQuantity())
                    throw new BadRequestException("Số lượng sản phẩm không đủ");

                // Query 2: save order detail
                orderDetailsRepository.save(OrderDetail.builder()
                        .productQuantity(productQuantity)
                        .price(cartInfo.getPrice())
                        .quantity(cartInfo.getQuantity())
                        .order(order)
                        .build());

                // Query 3: update product quantity
                productQuantity.setQuantity(productQuantity.getQuantity() - cartInfo.getQuantity());
                productQuantitiesRepository.save(productQuantity);

                // Query 4: delete shopping cart if exist
                if (cartInfo.getId() != null) shoppingCartRepository.deleteById(cartInfo.getId());
            }
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Thanh toán thất bại");
        }
    }

    @Override
    public Map<String, ?> singlePayment(int quantityID, Account currrentAccount, int quantity) {
        // Tim quantity product
        ProductQuantity productQuantity = productQuantitiesRepository
                .findById(quantityID)
                .orElseThrow(() -> new BadRequestException("Sản phẩm không tồn tại"));
        Integer discount = productQuantity.getProductDetail().getDiscount();
        int price = discount != null && discount > 0
                ? (productQuantity.getProductDetail().getPrice() * (100 - discount)) / 100
                : productQuantity.getProductDetail().getPrice();

        List<ShoppingCartInfo> shoppingCartInfos = new ArrayList<>();
        shoppingCartInfos.add(ShoppingCartInfo.builder()
                .size(productQuantity.getSize().getValue())
                .name(productQuantity.getProductDetail().getProduct().getName())
                .style(productQuantity.getProductDetail().getStyle().getValue())
                .price(price)
                .discount(discount)
                .quantity(quantity)
                .productQuantityId(quantityID)
                .build());

        return Map.of(
                CommonConstant.SHOPPING_CART_WRAPPER,
                new ShoppingCartWrapper(shoppingCartInfos),
                CommonConstant.TOTAL_PRICE,
                price * quantity);
    }
}
