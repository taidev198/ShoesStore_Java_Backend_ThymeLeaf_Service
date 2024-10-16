package com.taidev198.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.taidev198.bean.ProductFilterInfo;
import com.taidev198.model.ProductDetail;
import com.taidev198.model.ProductQuantity;
import com.taidev198.repository.customization.ProductDetailsCustomRepository;
import com.taidev198.service.FilterService;
import com.taidev198.util.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {
    @Autowired
    private ProductDetailsCustomRepository repo;

    @Override
    public Page<ProductFilterInfo> getProductFilterInfos(
            List<Integer> listStyleId,
            List<Integer> listCategoryId,
            List<Integer> listMaterialId,
            List<Integer> listColorId,
            int genderFilter,
            String query,
            Pageable pageable) {

        Page<ProductDetail> productDetailsPage = repo.findProductByFilter(
                listStyleId, listCategoryId, listMaterialId, listColorId, genderFilter, query, pageable);

        return productDetailsPage.map(productDetail -> ProductFilterInfo.builder()
                .id(productDetail.getId())
                .productId(productDetail.getProduct().getId())
                .name(productDetail.getProduct().getName())
                .discount(productDetail.getDiscount())
                .gender(productDetail.getGender().toString())
                .description(productDetail.getDescription())
                .category(productDetail.getProduct().getCategory().getValue())
                .style(productDetail.getStyle().getValue())
                .material(productDetail.getProduct().getMaterial().getValue())
                .price(CommonUtils.formatToVND(productDetail.getPrice()))
                .originPrice(CommonUtils.formatToVND(productDetail.getOriginPrice()))
                .discountPrice(
                        CommonUtils.formatToVND(productDetail.getPrice() * (100 - productDetail.getDiscount()) / 100))
                .color(productDetail.getColor().getValue())
                .images(productDetail.getImages())
                .sumQuantity(productDetail.getQuantities().stream()
                        .mapToInt(ProductQuantity::getQuantity)
                        .sum())
                .build());
    }
}
