package com.store.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters long")
    @NotNull(message = "Name must not be empty")
    private String name;

    @Column(name = "type")
    @Size(min = 3, max = 255, message = "Type must be between 3 and 255 characters long")
    @NotNull(message = "Type must not be null")
    private String type;

    @Column(name = "count")
    @Min(value = 0, message = "Count must be greater or equal to zero")
    private int count;

    @Column(name = "price")
    @NotNull(message = "Price must not be null")
    @Min(value = 0, message = "Price must be greater than zero")
    private BigDecimal price;

    @Column(name = "minPrice")
    @NotNull(message = "Minimal price must not be null")
    @Min(value = 0, message = "Minimal price must be greater than zero")
    private BigDecimal minPrice;

    @Column(name = "isOnSale")
    private boolean isOnSale;

    @Column(name = "discountPercent")
    @Min(value = 0, message = "Discount percent must be greater or equal to zero")
    private double discountPercent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(boolean isOnSale) {
        this.isOnSale = isOnSale;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }
}
