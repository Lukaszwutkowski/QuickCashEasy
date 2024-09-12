package com.retailtech.quickcasheasy.payment;

import java.math.BigDecimal;

class Payment {

    private Long id;
    private String paymentType;
    private BigDecimal amount;
    private boolean success;

    public Payment(Long id, String paymentType, BigDecimal amount) {
        this.id = id;
        this.paymentType = paymentType;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
