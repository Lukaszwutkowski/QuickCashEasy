package com.retailtech.quickcasheasy.payment;

import java.math.BigDecimal;
import java.util.List;

class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    public void processPayment(Payment payment, BigDecimal totalAmount) {
        if (payment.getAmount().compareTo(totalAmount) >= 0) {
            payment.setSuccess(true);
        } else {
            payment.setSuccess(false);
        }
        paymentRepository.save(payment);
    }

    public void deletePayment(Long paymentId) {
        paymentRepository.delete(paymentId);
    }
}
