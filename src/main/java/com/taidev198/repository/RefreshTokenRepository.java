package com.taidev198.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taidev198.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    List<RefreshToken> findByAccount_Id(int accountId);
}
