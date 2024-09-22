package com.retailtech.quickcasheasy.payment;

import com.retailtech.quickcasheasy.payment.dto.PaymentDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade for payment-related operations.
 * Provides a simplified interface to interact with payments.
 */
public class PaymentFacade {

    private final PaymentService paymentService;

    /**
     * Constructor initializing the PaymentService.
     */
    public PaymentFacade(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Creates a new payment.
     *
     * @param amount the amount of the payment
     * @param method the payment method
     * @return the created PaymentDTO
     */
    public PaymentDTO createPayment(Long id, BigDecimal amount, String method, String status, boolean success) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Payment payment = paymentService.createPayment(id, amount, method, status, success);
        return mapToDTO(payment);
    }

    /**
     * Retrieves a payment by its ID.
     *
     * @param paymentId the ID of the payment
     * @return the PaymentDTO if found, or null if not found
     */
    public PaymentDTO getPaymentById(Long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        return (payment != null) ? mapToDTO(payment) : null;
    }

    /**
     * Retrieves all payments.
     *
     * @return a list of PaymentDTOs
     */
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates the status and success flag of a payment.
     *
     * @param paymentId the ID of the payment
     * @param status    the new status
     * @param success   the success flag
     */
    public void updatePaymentStatus(Long paymentId, String status, boolean success) {
        paymentService.updatePaymentStatus(paymentId, status, success);
    }

    /**
     * Deletes a payment by its ID.
     *
     * @param paymentId the ID of the payment to delete
     */
    public void deletePayment(Long paymentId) {
        paymentService.deletePayment(paymentId);
    }

    /**
     * Private helper method to convert a Payment to a PaymentDTO.
     *
     * @param payment the Payment object
     * @return the corresponding PaymentDTO
     */
    private PaymentDTO mapToDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.isSuccess()
        );
    }
}
