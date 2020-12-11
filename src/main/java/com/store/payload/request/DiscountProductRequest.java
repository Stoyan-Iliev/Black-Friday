package com.store.payload.request;

import javax.validation.constraints.Min;

public class DiscountProductRequest {
    private long id;

    @Min(value = 0, message = "Discount percent must be greater than zero")
    private double discountPercent;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }
}
