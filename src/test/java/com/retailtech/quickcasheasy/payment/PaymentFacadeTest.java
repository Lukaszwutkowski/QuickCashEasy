package com.retailtech.quickcasheasy.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PaymentFacadeTest {

    private PaymentFacade paymentFacade;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = mock(PaymentService.class);
        paymentFacade = new PaymentFacade(paymentService);
    }

    @Test
    void it_should_process_successful_payment_via_facade() {
        // Given
        String paymentType = "CASH";
        BigDecimal amount = new BigDecimal("100.00"); // The payment amount
        BigDecimal totalAmount = new BigDecimal("50.00"); // The total transaction amount

        // Create an ArgumentCaptor to capture the Payment argument passed to the service
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        // Simulate the behavior of processPayment method in PaymentService
        doAnswer(invocation -> {
            // Capture the Payment object passed to the method
            Payment passedPayment = invocation.getArgument(0);
            passedPayment.setSuccess(true);  // Set the payment as successful
            return null;
        }).when(paymentService).processPayment(paymentCaptor.capture(), eq(totalAmount));

        // When: Call the processPayment method via the facade
        boolean success = paymentFacade.processPayment(paymentType, amount, totalAmount);

        // Then: Verify the payment was processed successfully
        assertTrue(success);

        // Verify that processPayment was called exactly once with the correct arguments
        verify(paymentService, times(1)).processPayment(paymentCaptor.capture(), eq(totalAmount));

        // Check the captured Payment object and verify its properties
        Payment capturedPayment = paymentCaptor.getValue();
        assertEquals(paymentType, capturedPayment.getPaymentType()); // Ensure payment type matches
        assertEquals(amount, capturedPayment.getAmount()); // Ensure payment amount matches
    }

    @Test
    void it_should_process_failed_payment_via_facade() {
        // Given
        String paymentType = "CASH";
        BigDecimal amount = new BigDecimal("30.00"); // Payment amount is less than totalAmount
        BigDecimal totalAmount = new BigDecimal("50.00");
        Payment payment = new Payment(null, paymentType, amount);

        // Simulate a failed payment
        doAnswer(invocationOnMock -> {
            Payment passedPayment = invocationOnMock.getArgument(0); // Capture the Payment argument
            passedPayment.setSuccess(false);  // Set payment to unsuccessful
            return null;
        }).when(paymentService).processPayment(any(Payment.class), eq(totalAmount));

        // When
        boolean success = paymentFacade.processPayment(paymentType, amount, totalAmount);

        // Then
        assertFalse(success);  // Expecting the payment to fail
        verify(paymentService, times(1)).processPayment(any(Payment.class), eq(totalAmount)); // Verify processPayment was called once
    }

    @Test
    void it_should_get_payment_by_id_via_facade() {
        // Given
        Payment payment = new Payment(1L, "CASH", BigDecimal.valueOf(100.0)); // Payment with id 1
        when(paymentService.getPaymentById(1L)).thenReturn(payment); // Mock returning the payment

        // When
        Payment foundPayment = paymentFacade.getPaymentById(1L);

        // Then
        assertNotNull(foundPayment); // The returned payment should not be null
        assertEquals(1L, foundPayment.getId()); // The payment ID should match the given ID
    }

    @Test
    void it_should_delete_payment_by_id_via_facade() {
        // Given
        Long paymentId = 1L; // ID of the payment to be deleted

        // When
        paymentFacade.deletePayment(paymentId);

        // Then
        verify(paymentService, times(1)).deletePayment(paymentId); // Verify that deletePayment was called once with the correct ID
    }


}
