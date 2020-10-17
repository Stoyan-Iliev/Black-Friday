package com.store.payload.request;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
@Validated
public class BoughtProductRequest {
    @Min(value = 1, message = "Id must be greater than zero")
    private long id;

    @Min(value = 1, message = "The count must be greater than zero")
    private int count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
