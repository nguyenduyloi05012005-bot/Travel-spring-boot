package com.Travel.repository;

import com.Travel.domain.AccountStatus;
import com.Travel.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    Seller findByEmail(String email);
    boolean existsByEmail(String email);
    List<Seller> findByStatus(AccountStatus status);
}
