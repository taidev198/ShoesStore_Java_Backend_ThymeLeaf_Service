package com.taidev198.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taidev198.model.ProductImage;

public interface ProductImagesRepository extends JpaRepository<ProductImage, Integer> {}
