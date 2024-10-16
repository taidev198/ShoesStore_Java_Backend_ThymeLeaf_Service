package com.taidev198.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.taidev198.bean.ProductFilterInfo;

public interface FilterService {
    Page<ProductFilterInfo> getProductFilterInfos(
            List<Integer> listStyleId,
            List<Integer> listCategoryId,
            List<Integer> listMaterialId,
            List<Integer> listColorId,
            int genderFilter,
            String query,
            Pageable pageable);
}
