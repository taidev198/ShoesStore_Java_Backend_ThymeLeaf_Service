package com.taidev198.repository.customization.impl;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.taidev198.model.Enum.ProductGender;
import com.taidev198.model.ProductDetail;
import com.taidev198.repository.base.BaseRepository;
import com.taidev198.repository.base.WhereClauseType;
import com.taidev198.repository.base.WhereElements;
import com.taidev198.repository.customization.ProductDetailsCustomRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductDetailsCustomRepositoryImpl implements ProductDetailsCustomRepository {
    private final BaseRepository<ProductDetail> baseRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public BaseRepository<ProductDetail> getRepository() {
        return baseRepository;
    }

    @Override
    public Page<ProductDetail> findByProductName(String name, Pageable pageable) {
        return baseRepository.fetchAllDataWithPagination(
                List.of(new WhereElements("product.name", "%" + name + "%", WhereClauseType.LIKE_IGNORE_CASE)),
                pageable);
    }

    @Override
    public Page<ProductDetail> findProductByFilter(
            List<Integer> listStyleId,
            List<Integer> listCategoryId,
            List<Integer> listMaterialId,
            List<Integer> listColorId,
            int genderFilter,
            String query,
            Pageable pageable) {

        List<WhereElements> whereElements = new ArrayList<>();
        if (!listStyleId.isEmpty()) whereElements.add(new WhereElements("style.id", listStyleId, WhereClauseType.IN));
        if (!listCategoryId.isEmpty())
            whereElements.add(new WhereElements("product.category.id", listCategoryId, WhereClauseType.IN));
        if (!listMaterialId.isEmpty())
            whereElements.add(new WhereElements("product.material.id", listMaterialId, WhereClauseType.IN));
        if (!listColorId.isEmpty()) whereElements.add(new WhereElements("color.id", listColorId, WhereClauseType.IN));
        switch (genderFilter) {
            case 2:
                whereElements.add(new WhereElements("gender", ProductGender.MALE, WhereClauseType.EQUAL));
                break;
            case 3:
                whereElements.add(new WhereElements("gender", ProductGender.FEMALE, WhereClauseType.EQUAL));
                break;
        }
        if (!query.isEmpty()) {
            whereElements.add(new WhereElements("product.name", "%" + query + "%", WhereClauseType.LIKE_IGNORE_CASE));
        }

        whereElements.add(new WhereElements("deletedAt", null, WhereClauseType.IS_NULL));

        return baseRepository.fetchAllDataWithPagination(whereElements, pageable);
    }
}
