package com.retailtech.quickcasheasy.payment;

import java.math.BigDecimal;

public class PaymentFacade {

    private final PaymentService paymentService;

    // Constructor for PaymentFacade, injecting PaymentService
    public PaymentFacade(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Processes a payment.
     *
     * @param paymentType the type of payment (e.g., CASH, CARD)
     * @param amount the amount of payment
     * @param totalAmount the total amount that needs to be paid
     * @return true if the payment is successful, false otherwise
     */
    public boolean processPayment(String paymentType, BigDecimal amount, BigDecimal totalAmount) {
        // Create a new Payment object
        Payment payment = new Payment(null, paymentType, amount);

        // Process the payment using the PaymentService
        paymentService.processPayment(payment, totalAmount);

        // Return whether the payment was successful
        return payment.isSuccess();
    }

    /**
     * Retrieves a payment by its ID.
     *
     * @param id the ID of the payment
     * @return the Payment object
     */
    public Payment getPaymentById(Long id) {
        // Get payment by ID from the PaymentService
        return paymentService.getPaymentById(id);
    }

    /**
     * Deletes a payment by its ID.
     *
     * @param id the ID of the payment to delete
     */
    public void deletePayment(Long id) {
        // Call the PaymentService to delete the payment
        paymentService.deletePayment(id);
    }

}
