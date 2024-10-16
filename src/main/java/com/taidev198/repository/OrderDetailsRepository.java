package com.taidev198.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taidev198.model.OrderDetail;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Integer> {}
