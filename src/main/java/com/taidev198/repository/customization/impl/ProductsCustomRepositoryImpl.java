package com.taidev198.repository.customization.impl;

import org.springframework.stereotype.Repository;

import com.taidev198.model.Product;
import com.taidev198.repository.base.BaseRepository;
import com.taidev198.repository.customization.ProductsCustomRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductsCustomRepositoryImpl implements ProductsCustomRepository {
    private final BaseRepository<Product> baseRepository;

    @Override
    public BaseRepository<Product> getRepository() {
        return baseRepository;
    }
}
