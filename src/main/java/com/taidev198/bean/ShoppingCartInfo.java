package com.taidev198.bean;

import com.taidev198.model.ProductDetail;
import com.taidev198.model.ProductImage;
import com.taidev198.model.ProductQuantity;
import com.taidev198.model.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartInfo {
    private Integer id;
    private Integer customerId;
    private Integer productId;
    private Integer productDetailId;
    private Integer productQuantityId;
    private int quantity;
    private String style;
    private int priceProduct;
    private int price;
    private String size;
    private String name;
    private Integer discount;
    private List<ProductImage> images;
    private ProductQuantity sizeQuantity;
    private ProductDetail productDetail;
    private List<ProductQuantity> quantities;

    public static ShoppingCartInfo fromShoppingCart(ShoppingCart shoppingCart) {
        return ShoppingCartInfo.builder()
            .id(shoppingCart.getId())
            .customerId(shoppingCart.getAccount().getId())
            .productId(shoppingCart.getProductQuantity().getProductDetail().getProduct().getId())
            .productDetailId(shoppingCart.getProductQuantity().getProductDetail().getId())
            .productQuantityId(shoppingCart.getProductQuantity().getId())
            .quantity(shoppingCart.getQuantity())
            .style(shoppingCart.getProductQuantity().getProductDetail().getStyle().getValue())
            .priceProduct(shoppingCart.getProductQuantity().getProductDetail().getPrice())
            .price(shoppingCart.getQuantity() * shoppingCart.getProductQuantity().getProductDetail().getPrice())
            .size(shoppingCart.getProductQuantity().getSize().getValue())
            .name(shoppingCart.getProductQuantity().getProductDetail().getProduct().getName())
            .discount(shoppingCart.getProductQuantity().getProductDetail().getDiscount())
            .images(shoppingCart.getProductQuantity().getProductDetail().getImages())
            .sizeQuantity(shoppingCart.getProductQuantity())
            .build();
    }

    public String getProductURL() {
        return "/products/" + productDetailId;
    }

    public String getOriginPriceFormated() {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return formatter.format(this.priceProduct) + " VND";
    }

    public int getDiscountPrice() {
        int discountedPrice = this.priceProduct;
        if (discount != null && discount > 0) {
            discountedPrice = this.priceProduct - (this.priceProduct * this.discount / 100);
        }
        return discountedPrice;
    }

    public String getDiscountPriceFormated() {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        return numberFormat.format(getDiscountPrice()) + " VND";
    }

    public String getTotalDiscountPriceFormated() {
        int totalDiscountedPrice = getDiscountPrice() * this.quantity;
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        return numberFormat.format(totalDiscountedPrice) + " VND";
    }
}
