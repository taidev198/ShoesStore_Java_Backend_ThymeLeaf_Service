package com.taidev198.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taidev198.model.Constant;

@Repository
public interface ConstantRepository extends JpaRepository<Constant, Integer> {
    List<Constant> findByType(String type);
}
