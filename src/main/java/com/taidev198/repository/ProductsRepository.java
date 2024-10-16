package com.taidev198.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taidev198.model.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {}
