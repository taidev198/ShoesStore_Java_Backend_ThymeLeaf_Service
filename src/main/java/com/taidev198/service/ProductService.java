package com.taidev198.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.taidev198.bean.*;
import com.taidev198.model.ProductDetail;
import com.taidev198.model.ProductImage;

public interface ProductService {
    ProductDetailInfo getProductDetailById(Integer id);

    List<ProductDetailColors> getProductDetailColors(Integer id);

    Page<ProductDetail> getProductsByName(String name, int page, int size);

    ProductDetailInfoSeller getProductDetailByIdForManager(Integer id);

    ProductDetailEdit getProductDetailEditById(Integer id);

    List<ProductImage> getProductImagesByProductDetailId(Integer id);

    void updateProductImages(Integer productDetailId, List<MultipartFile> imagesToAdd, List<Integer> imagesToRemove);

    List<String> findAllCategories();

    List<String> findAllStyles();

    List<String> findAllMaterials();

    ProductDetail updateProductInfo(ProductDetailEdit productDetailEdit);

    List<ProductExcel> importProducts(MultipartFile file) throws Exception;

    ProductDetail deleteProductDetailById(Integer id);
}
