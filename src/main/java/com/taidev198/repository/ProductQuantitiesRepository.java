package com.taidev198.repository;

import com.taidev198.model.ProductDetail;
import com.taidev198.model.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductQuantitiesRepository extends JpaRepository<ProductQuantity, Integer> {
    List<ProductQuantity> findAllByProductDetail(ProductDetail productDetail);

    @Modifying
    @Query("UPDATE ProductQuantity pq SET pq.quantity = :quantity WHERE pq.id = :id")
    void updateQuantity(@Param("id") Integer id, @Param("quantity") int quantity);
}
