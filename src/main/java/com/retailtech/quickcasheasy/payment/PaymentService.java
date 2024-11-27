package com.retailtech.quickcasheasy.payment;

import com.retailtech.quickcasheasy.database.DatabaseUtils;
import com.retailtech.quickcasheasy.exception.PaymentNotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Internal service handling payment business logic.
 * This class is package-private.
 */
class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BankPaymentClient bankPaymentClient;

    PaymentService() {
        this.paymentRepository = new PaymentRepositoryImpl(new DatabaseUtils());
        this.bankPaymentClient = new BankPaymentClient();
    }

    /**
     * Creates a new payment.
     *
     * @param amount the amount of the payment
     * @param method the payment method
     * @return the created Payment
     */
    Payment createPayment(Long id, BigDecimal amount, String method, String status, boolean success) {
        Payment payment = new Payment(id, amount, method, status, success);
        paymentRepository.save(payment); // Save to repository
        return payment;
    }

    /**
     * Retrieves a payment by its ID.
     *
     * @param paymentId the ID of the payment
     * @return the Payment if found, or null if not found
     */
    Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId).orElse(null);
    }

    /**
     * Retrieves all payments.
     *
     * @return a list of all Payments
     */
    List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Updates the status and success flag of a payment.
     *
     * @param paymentId the ID of the payment
     * @param status    the new status
     * @param success   the success flag
     * @throws PaymentNotFoundException if the payment is not found
     */
    void updatePaymentStatus(Long paymentId, String status, boolean success) {
        Payment payment = getPaymentById(paymentId);
        if (payment != null) {
            payment.setStatus(status);
            payment.setSuccess(success);
            paymentRepository.save(payment); // Update in repository
        } else {
            throw new PaymentNotFoundException(paymentId);
        }
    }

    /**
     * Deletes a payment by its ID.
     *
     * @param paymentId the ID of the payment to delete
     * @throws PaymentNotFoundException if the payment is not found
     */
    void deletePayment(Long paymentId) {
        if (paymentRepository.existsById(paymentId)) {
            paymentRepository.delete(paymentId);
        } else {
            throw new PaymentNotFoundException(paymentId);
        }
    }

    /**
     * Initiates an external payment by calling the bank's API and saving the transaction locally.
     *
     * @param id          the ID of the transaction
     * @param amount      the amount to be paid
     * @param method      the payment method
     * @param cardNumber  the card number associated with the primary account to debit
     * @return the response from the bank's API
     * @throws RuntimeException if there is an error processing the payment
     */
    String initiateExternalPayment(Long id, BigDecimal amount, String method, int cardNumber) {
        try {
            // Call the bank API with the card number and amount
            String response = bankPaymentClient.makeBankPayment(cardNumber, amount);

            // Create and save the local transaction
            Payment payment = new Payment(id, amount, method, "Completed", true);
            paymentRepository.save(payment);

            return response; // Return the response from the bank system
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to process payment", e);
        }
    }

}
