package com.store.payload.request;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Set;

public class PurchaseRequest {
    @Min(value = 1, message = "User id must be greater than zero")
    private long userId;

    @Valid
    private Set<BoughtProductRequest> boughtProducts;

    @NotBlank(message = "The address must not be blank")
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
