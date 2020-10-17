package com.store.payload.request;

import java.util.Set;

public class PurchaseRequest {
    private long userId;
    private Set<BoughtProductRequest> boughtProducts;
    private String address;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Set<BoughtProductRequest> getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(Set<BoughtProductRequest> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
