package com.taidev198.repository.customization.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.taidev198.model.ShoppingCart;
import com.taidev198.repository.base.BaseRepository;
import com.taidev198.repository.base.WhereClauseType;
import com.taidev198.repository.base.WhereElements;
import com.taidev198.repository.customization.ShoppingCartsCustomRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ShoppingCartsCustomRepositoryImpl implements ShoppingCartsCustomRepository {
    private final BaseRepository<ShoppingCart> baseRepository;

    @Override
    public BaseRepository<ShoppingCart> getRepository() {
        return baseRepository;
    }

    @Override
    public Page<ShoppingCart> findAllByAccountId(Integer accountId, Pageable pageable) {
        return baseRepository.fetchAllDataWithPagination(
                List.of(new WhereElements("account.id", accountId, WhereClauseType.EQUAL)), pageable);
    }

    @Override
    public List<ShoppingCart> findAllByAccountId(Integer accountId) {
        return baseRepository.fetchAllDataWithoutPagination(
                List.of(new WhereElements("account.id", accountId, WhereClauseType.EQUAL)), null);
    }
}
