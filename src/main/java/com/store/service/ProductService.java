package com.store.service;

import com.store.entity.Product;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product addProduct(Product product){
        return productRepository.save(product);
    }

    public List<Product> getAllProductsOnSale(){
        return productRepository.getAllByIsOnSaleIsTrue();
    }

    public List<Product> addProducts(List<Product> products){
        return productRepository.saveAll(products);
    }
}
