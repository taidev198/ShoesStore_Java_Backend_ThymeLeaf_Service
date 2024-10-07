package com.taidev198.repository.customization;

import com.taidev198.model.Constant;
import com.taidev198.model.ProductDetail;
import com.taidev198.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
