package com.taidev198.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import jakarta.annotation.Nullable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.taidev198.bean.OrderInfo;
import com.taidev198.bean.RevenueInfo;
import com.taidev198.model.Account;
import com.taidev198.model.Enum.OrderStatus;

public interface OrdersService {
    Page<OrderInfo> getOrders(@Nullable Integer accountId, @Nullable OrderStatus status, Pageable pageable);

    void changeOrderStatus(Account account, Integer orderId, String status);

    long countByDate(LocalDate date);

    double sumTotalPriceByDate(LocalDate date);

    double sumEstimatedRevenueByDate(LocalDate date);

    RevenueInfo getMonthlyRevenue(YearMonth yearMonth);

    Map<String, RevenueInfo> getDailyRevenueDetails(YearMonth yearMonth);

    Map<String, RevenueInfo> getMonthlyRevenueDetails(int year);
}
