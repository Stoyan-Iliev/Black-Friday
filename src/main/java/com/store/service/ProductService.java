package com.store.service;

import com.store.entity.Product;
import com.store.exception.ProductNotFoundException;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> addProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.getAllByCountGreaterThan(0);
        setDiscountPriceForAllProductsOnSale(products);
        return products;
    }

    public List<Product> getAllProductsOnSale() {
        List<Product> products = productRepository.getAllByIsOnSaleIsTrue();
        setDiscountPriceForAllProductsOnSale(products);
        return products;
    }

    private void setDiscountPriceForAllProductsOnSale(List<Product> products) {
        if (isNull(products)) {
            return;
        }

        for (Product product : products) {
            setNewPriceIfNeeded(product);
        }
    }

    private boolean isNull(List<Product> products) {
        return products == null;
    }

    private void setNewPriceIfNeeded(Product product) {
        if (product.isOnSale()) {
            product.setPrice(calculateNewPrice(product));
        }
    }

    private BigDecimal calculateNewPrice(Product product) {
        BigDecimal newPrice = calculatePriceWithDiscount(product);
        newPrice = ensurePriceIsNotBelowMinPrice(product, newPrice);
        return newPrice;
    }

    private BigDecimal ensurePriceIsNotBelowMinPrice(Product product, BigDecimal newPrice) {
        if (isNewPricePriceLessThanMinPrice(newPrice, product.getMinPrice())) {
            newPrice = product.getMinPrice();
        }
        return newPrice;
    }

    private boolean isNewPricePriceLessThanMinPrice(BigDecimal first, BigDecimal second) {
        return second.compareTo(first) < 0;
    }

    private BigDecimal calculatePriceWithDiscount(Product product) {
        return product.getPrice()
                .subtract(getDiscountSum(product));
    }

    private BigDecimal getDiscountSum(Product product) {
        return product.getPrice()
                .multiply(BigDecimal.valueOf(product.getDiscountPercent() / 100));
    }

    public Product getProductById(long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id = " + id + " does not exist"));
    }

    public Product updateProduct(long id, Product product) {
        getProductById(id);
        product.setId(id);
        return productRepository.save(product);
    }
}
