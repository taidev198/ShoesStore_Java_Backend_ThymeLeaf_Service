package com.taidev198.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.taidev198.model.Account;
import com.taidev198.model.Enum.AccountRole;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT a FROM Account a WHERE " + "(LOWER(a.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR "
            + "LOWER(a.displayName) LIKE LOWER(CONCAT('%', :query, '%')) OR "
            + "LOWER(a.email) LIKE LOWER(CONCAT('%', :query, '%'))) "
            + "AND (:role IS NULL OR a.role IN :role)")
    Page<Account> findByFilter(String query, List<AccountRole> role, Pageable pageable);
    Optional<Account> findAccountById(Integer senderId);
}
