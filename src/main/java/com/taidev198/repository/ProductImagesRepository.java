package com.taidev198.repository;

import com.taidev198.model.Product;
import com.taidev198.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImagesRepository extends JpaRepository<ProductImage, Integer> {
}