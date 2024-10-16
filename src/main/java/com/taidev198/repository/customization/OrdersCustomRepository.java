package com.taidev198.repository.customization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.taidev198.model.Enum.OrderStatus;
import com.taidev198.model.Order;
import com.taidev198.repository.base.BaseRepository;

public interface OrdersCustomRepository extends CustomRepository<Order, Integer, BaseRepository<Order>> {
    Page<Order> findAllByAccountId(Integer accountId, Pageable pageable);

    Page<Order> findAllByAccountIdAndStatus(Integer accountId, OrderStatus status, Pageable pageable);

    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);
}
