package com.taidev198.service;

import com.taidev198.bean.ProductFilterInfo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
