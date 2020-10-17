package com.store.repository;

import com.store.entity.BoughtProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoughtProductRepository extends JpaRepository<BoughtProduct, Long> {
}
