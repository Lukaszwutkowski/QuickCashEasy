package com.retailtech.quickcasheasy.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long paymentId) {
        super("Payment not found for id: " + paymentId);
    }
}
