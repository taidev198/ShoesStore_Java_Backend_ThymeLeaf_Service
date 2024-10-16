package com.taidev198.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.taidev198.model.Enum.OrderStatus;
import com.taidev198.model.Order;

public interface OrdersRepository extends JpaRepository<Order, Integer> {

    long countByUpdatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.updatedAt BETWEEN :start AND :end AND o.status = :status")
    Optional<Double> sumTotalPriceByUpdatedAtBetweenAndStatus(
            @Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("status") OrderStatus status);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.updatedAt BETWEEN :start AND :end AND o.status IN :statuses")
    Optional<Double> sumTotalPriceByUpdatedAtBetweenAndStatuses(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("statuses") List<OrderStatus> statuses);

    List<Order> findByUpdatedAtBetween(LocalDateTime start, LocalDateTime end);
}
