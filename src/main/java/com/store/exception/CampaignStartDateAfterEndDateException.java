package com.store.exception;

public class CampaignStartDateAfterEndDateException extends RuntimeException {
    public CampaignStartDateAfterEndDateException(String message) {
        super(message);
    }
}
