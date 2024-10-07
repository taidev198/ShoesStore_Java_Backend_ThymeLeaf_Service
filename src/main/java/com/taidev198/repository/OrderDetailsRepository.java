package com.taidev198.repository;

import com.taidev198.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Integer> {
}
