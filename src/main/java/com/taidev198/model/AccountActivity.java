package com.taidev198.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "account_activity")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class AccountActivity extends EntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long accountId;
    private String activity;
}
