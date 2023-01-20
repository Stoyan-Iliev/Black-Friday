package com.store.repository;

import com.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getAllByIsOnSaleIsTrue();
    List<Product> getAllByCountGreaterThanOrderByType(int count);
    List<Product> getAllByCountGreaterThanAndType(int count, String type);

}
