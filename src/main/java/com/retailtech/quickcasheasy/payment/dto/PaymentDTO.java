package com.retailtech.quickcasheasy.payment.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for payment information.
 */
public class PaymentDTO {

    private final Long id;
    private final BigDecimal amount;
    private final String method;
    private final String status;
    private final boolean success;  // Added success field

    /**
     * Constructor to initialize all fields.
     *
     * @param id      the unique identifier of the payment
     * @param amount  the amount of the payment
     * @param method  the payment method (e.g., Credit Card, PayPal)
     * @param status  the status of the payment (e.g., PENDING, COMPLETED)
     * @param success the success flag of the payment
     */
    public PaymentDTO(Long id, BigDecimal amount, String method, String status, boolean success) {
        this.id = id;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.success = success;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public String getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }
}
