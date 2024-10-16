package com.taidev198.repository.customization.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.taidev198.model.Enum.OrderStatus;
import com.taidev198.model.Order;
import com.taidev198.repository.base.BaseRepository;
import com.taidev198.repository.base.WhereClauseType;
import com.taidev198.repository.base.WhereElements;
import com.taidev198.repository.customization.OrdersCustomRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrdersCustomRepositoryImpl implements OrdersCustomRepository {
    private final BaseRepository<Order> baseRepository;

    @Override
    public BaseRepository<Order> getRepository() {
        return baseRepository;
    }

    @Override
    public Page<Order> findAllByAccountId(Integer accountId, Pageable pageable) {
        return baseRepository.fetchAllDataWithPagination(
                List.of(new WhereElements("account.id", accountId, WhereClauseType.EQUAL)), pageable);
    }

    @Override
    public Page<Order> findAllByAccountIdAndStatus(Integer accountId, OrderStatus status, Pageable pageable) {
        return baseRepository.fetchAllDataWithPagination(
                List.of(
                        new WhereElements("account.id", accountId, WhereClauseType.EQUAL),
                        new WhereElements("status", status, WhereClauseType.EQUAL)),
                pageable);
    }

    @Override
    public Page<Order> findAllByStatus(OrderStatus status, Pageable pageable) {
        return baseRepository.fetchAllDataWithPagination(
                List.of(new WhereElements("status", status, WhereClauseType.EQUAL)), pageable);
    }
}
