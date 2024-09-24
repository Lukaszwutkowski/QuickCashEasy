package com.retailtech.quickcasheasy.payment;

import com.retailtech.quickcasheasy.exception.PaymentNotFoundException;
import com.retailtech.quickcasheasy.payment.dto.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for PaymentFacade.
 * Focuses on testing the facade's public methods using DTOs.
 */
@ExtendWith(MockitoExtension.class)
class PaymentFacadeTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentFacade paymentFacade;

    private Payment payment1;
    private Payment payment2;

    @BeforeEach
    void setUp() {
        payment1 = new Payment(1L, BigDecimal.valueOf(30.00), "Credit Card", "COMPLETED", true);
        payment2 = new Payment(2L, BigDecimal.valueOf(45.00), "PayPal", "PENDING", false);
    }

    @Test
    @DisplayName("Should create and return a new payment")
    void it_should_create_and_return_payment() {
        // Given
        Long id = 1L;
        BigDecimal amount = BigDecimal.valueOf(100.00);
        String method = "Credit Card";
        String status = "PENDING";
        boolean success = false;

        Payment payment = new Payment(id, amount, method, status, success);
        when(paymentService.createPayment(id, amount, method, status, success)).thenReturn(payment);

        // When
        PaymentDTO paymentDTO = paymentFacade.createPayment(id, amount, method, status, success);

        // Then
        assertNotNull(paymentDTO, "PaymentDTO should not be null");
        assertEquals(amount, paymentDTO.getAmount(), "Amounts should match");
        assertEquals(method, paymentDTO.getMethod(), "Methods should match");
        assertEquals("PENDING", paymentDTO.getStatus(), "Status should be 'PENDING'");
        assertFalse(paymentDTO.isSuccess(), "Success flag should be false by default");

        verify(paymentService, times(1)).createPayment(id, amount, method, status, success);
    }

    @Test
    @DisplayName("Should update payment status and success flag")
    void it_should_update_payment_status_and_success() {
        // Given
        Long paymentId = 1L;
        String newStatus = "COMPLETED";
        boolean newSuccess = true;

        // Mock the behavior of updatePaymentStatus to do nothing (void method)
        doNothing().when(paymentService).updatePaymentStatus(paymentId, newStatus, newSuccess);

        // Mock getPaymentById to return the updated Payment
        Payment updatedPayment = new Payment(paymentId, BigDecimal.valueOf(75.00), "Debit Card", newStatus, newSuccess);
        when(paymentService.getPaymentById(paymentId)).thenReturn(updatedPayment);

        // When
        paymentFacade.updatePaymentStatus(paymentId, newStatus, newSuccess);

        // Then
        verify(paymentService, times(1)).updatePaymentStatus(paymentId, newStatus, newSuccess);

        // Verify the updated payment via getPaymentById
        PaymentDTO updatedPaymentDTO = paymentFacade.getPaymentById(paymentId);
        assertNotNull(updatedPaymentDTO, "Updated PaymentDTO should not be null");
        assertEquals(newStatus, updatedPaymentDTO.getStatus(), "Status should be 'COMPLETED'");
        assertTrue(updatedPaymentDTO.isSuccess(), "Success flag should be true");
    }

    @Test
    @DisplayName("Should retrieve all payments")
    void it_should_get_all_payments() {
        // Given
        List<Payment> mockPayments = Arrays.asList(payment1, payment2);
        when(paymentService.getAllPayments()).thenReturn(mockPayments);

        // When
        List<PaymentDTO> payments = paymentFacade.getAllPayments();

        // Then
        assertNotNull(payments, "Payments list should not be null");
        assertEquals(2, payments.size(), "Should retrieve 2 payments");

        assertTrue(payments.stream().anyMatch(p -> p.getAmount().compareTo(BigDecimal.valueOf(30.00)) == 0
                && p.getMethod().equals("Credit Card")), "Payments should include the first payment");

        assertTrue(payments.stream().anyMatch(p -> p.getAmount().compareTo(BigDecimal.valueOf(45.00)) == 0
                && p.getMethod().equals("PayPal")), "Payments should include the second payment");

        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    @DisplayName("Should throw PaymentNotFoundException when updating non-existent payment")
    void it_should_throw_payment_not_found_exception_when_updating_non_existent_payment() {
        // Given
        Long nonExistentId = 999L;
        doThrow(new PaymentNotFoundException(nonExistentId)).when(paymentService).updatePaymentStatus(nonExistentId, "FAILED", false);

        // When & Then
        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> {
            paymentFacade.updatePaymentStatus(nonExistentId, "FAILED", false);
        });

        assertEquals("Payment not found for id: 999", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when amount is negative")
    void it_should_throw_exception_when_amount_is_negative() {
        // Given
        BigDecimal negativeAmount = BigDecimal.valueOf(-10.00);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentFacade.createPayment(1L, negativeAmount, "Credit Card", "PENDING", false);
        });

        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    @DisplayName("Should retrieve payment by ID")
    void it_should_get_payment_by_id() {
        // Given
        Long paymentId = payment1.getId();
        when(paymentService.getPaymentById(paymentId)).thenReturn(payment1);

        // When
        PaymentDTO paymentDTO = paymentFacade.getPaymentById(paymentId);

        // Then
        assertNotNull(paymentDTO, "PaymentDTO should not be null");
        assertEquals(paymentId, paymentDTO.getId(), "Payment IDs should match");
        assertEquals(payment1.getMethod(), paymentDTO.getMethod(), "Methods should match");

        verify(paymentService, times(1)).getPaymentById(paymentId);
    }

    @Test
    @DisplayName("Should return null when retrieving non-existent payment")
    void it_should_return_null_when_payment_not_found() {
        // Given
        Long nonExistentId = 888L;
        when(paymentService.getPaymentById(nonExistentId)).thenReturn(null);

        // When
        PaymentDTO paymentDTO = paymentFacade.getPaymentById(nonExistentId);

        // Then
        assertNull(paymentDTO, "PaymentDTO should be null for non-existent payment");
        verify(paymentService, times(1)).getPaymentById(nonExistentId);
    }

    @Test
    @DisplayName("Should delete an existing payment")
    void it_should_delete_payment() {
        // Given
        Long paymentId = payment1.getId();

        // When
        paymentFacade.deletePayment(paymentId);

        // Then
        verify(paymentService, times(1)).deletePayment(paymentId);
    }

    @Test
    @DisplayName("Should throw PaymentNotFoundException when deleting non-existent payment")
    void it_should_throw_exception_when_deleting_non_existent_payment() {
        // Given
        Long nonExistentId = 999L;
        doThrow(new PaymentNotFoundException(nonExistentId)).when(paymentService).deletePayment(nonExistentId);

        // When & Then
        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> {
            paymentFacade.deletePayment(nonExistentId);
        });

        assertEquals("Payment not found for id: 999", exception.getMessage());
    }
}
