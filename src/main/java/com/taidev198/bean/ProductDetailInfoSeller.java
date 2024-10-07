package com.taidev198.bean;

import com.taidev198.model.Embeddables.ProductDescription;
import com.taidev198.model.ProductImage;
import com.taidev198.model.ProductQuantity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailInfoSeller {
    private Integer id;
    private Integer productId;
    private String name;
    private Integer discount;
    private String gender;
    private ProductDescription description;
    private String category;
    private String style;
    private String material;
    private String price;
    private String color;
    private List<ProductImage> images;
    private List<ProductQuantity> sizeQuantity;
    private String originPrice;
    private Integer totalQuantity;
    private String discountedPrice;
}