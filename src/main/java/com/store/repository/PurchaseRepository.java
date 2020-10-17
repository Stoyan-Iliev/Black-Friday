package com.store.repository;

import com.store.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByPurchaseDateBetweenOrderByPurchaseDate(LocalDate startDate, LocalDate endDate);
}
