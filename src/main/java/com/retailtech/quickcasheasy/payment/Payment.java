package com.retailtech.quickcasheasy.payment;

import java.math.BigDecimal;

/**
 * Internal representation of a payment.
 * This class is package-private and not exposed externally.
 */
class Payment {

    private Long id;  // Now mutable to accept IDs from the database
    private BigDecimal amount;
    private String method;
    private String status;
    private boolean success;

    // Constructor for new payments (without ID)
    Payment(BigDecimal amount, String method, String status, boolean success) {
        this.amount = amount;
        this.method = method;
        this.status = "PENDING";
        this.success = false;
    }

    // Constructor for payments loaded from the database (with ID)
    Payment(Long id, BigDecimal amount, String method, String status, boolean success) {
        this.id = id;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.success = success;
    }

    // Getters and Setters
    Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    BigDecimal getAmount() {
        return amount;
    }

    String getMethod() {
        return method;
    }

    String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    boolean isSuccess() {
        return success;
    }

    void setSuccess(boolean success) {
        this.success = success;
    }
}
