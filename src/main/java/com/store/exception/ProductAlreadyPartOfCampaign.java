package com.store.exception;

public class ProductAlreadyPartOfCampaign extends RuntimeException {
    public ProductAlreadyPartOfCampaign(String message) {
        super(message);
    }
}
