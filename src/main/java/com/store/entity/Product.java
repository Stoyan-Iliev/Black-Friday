package com.store.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    //@Size(min = 3, max = 255)
    //@NotEmpty
    private String name;

    @Column(name = "type")
    //@Size(min = 3, max = 255)
    //@NotEmpty
    private String type;

    @Column(name = "count")
    //@NotEmpty
    private int count;

    @Column(name = "price")
    //@NotEmpty
    private BigDecimal price;

    @Column(name = "minPrice")
    //@NotEmpty
    private BigDecimal minPrice;

    @Column(name = "isOnSale")
    //@NotEmpty
    private boolean isOnSale;

    @Column(name = "discountPercent")
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

    public void setIsOnSale(boolean onSale) {
        isOnSale = onSale;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }
}
