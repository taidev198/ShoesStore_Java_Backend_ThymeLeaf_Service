package com.taidev198.repository.customization;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.taidev198.model.ProductDetail;
import com.taidev198.repository.base.BaseRepository;

public interface ProductDetailsCustomRepository
        extends CustomRepository<ProductDetail, Integer, BaseRepository<ProductDetail>> {
    Page<ProductDetail> findByProductName(String name, Pageable pageable);

    Page<ProductDetail> findProductByFilter(
            List<Integer> listStyleId,
            List<Integer> listCategoryId,
            List<Integer> listMaterialId,
            List<Integer> listColorId,
            int genderFilter,
            String query,
            Pageable pageable);
}
