package com.taidev198.repository.customization.impl;

import org.springframework.stereotype.Repository;

import com.taidev198.model.ProductQuantity;
import com.taidev198.repository.base.BaseRepository;
import com.taidev198.repository.customization.ProductQuantitiesCustomRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductQuantitiesCustomRepositoryImpl implements ProductQuantitiesCustomRepository {
    private final BaseRepository<ProductQuantity> productQuantityBaseRepository;

    @Override
    public BaseRepository<ProductQuantity> getRepository() {
        return productQuantityBaseRepository;
    }
}
