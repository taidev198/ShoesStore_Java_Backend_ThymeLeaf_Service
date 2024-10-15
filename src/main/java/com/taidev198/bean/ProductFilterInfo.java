package com.taidev198.bean;

import com.taidev198.model.Embeddables.ProductDescription;
import com.taidev198.model.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductFilterInfo {
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
    private String discountPrice;
    private String originPrice;
    private String color;
    private List<ProductImage> images;
    private int sumQuantity;
}