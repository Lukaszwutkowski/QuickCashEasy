package com.retailtech.quickcasheasy.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentService paymentService;
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentService = new PaymentService(paymentRepository);
    }

    @Test
    void it_should_get_all_payments() {
        // Given
        Payment payment1 = new Payment(1L, "CASH", BigDecimal.valueOf(100.0));
        Payment payment2 = new Payment(2L, "CARD", BigDecimal.valueOf(150.0));
        when(paymentRepository.findAll()).thenReturn(List.of(payment1, payment2));

        // When
        List<Payment> payments = paymentService.getAllPayments();

        // Then
        assertNotNull(payments);
        assertEquals(2, payments.size());
    }

    @Test
    void it_should_get_payment_by_id() {
        // Given
        Payment payment1 = new Payment(1L, "CASH", BigDecimal.valueOf(100.0));
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment1));

        // When
        Payment foundPayment = paymentService.getPaymentById(1L);

        // Then
        assertNotNull(foundPayment);
        assertEquals(1L, foundPayment.getId());
        assertEquals("CASH", foundPayment.getPaymentType());
    }

    @Test
    void it_should_throw_exception_when_payment_not_found() {
        // Given
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> paymentService.getPaymentById(1L));
        assertEquals("Payment not found", exception.getMessage());
    }

    @Test
    void it_should_process_successful_payment() {
        // Given
        Payment payment1 = new Payment(1L, "CASH", BigDecimal.valueOf(100.0));
        BigDecimal totalAmount = BigDecimal.valueOf(100.0);

        // When
        paymentService.processPayment(payment1, totalAmount);

        // Then
        assertTrue(payment1.isSuccess());
        verify(paymentRepository, times(1)).save(payment1);
    }

    @Test
    void it_should_process_failed_payment() {
        // Given
        Payment payment1 = new Payment(1L, "CASH", BigDecimal.valueOf(100.0));
        BigDecimal totalAmount = BigDecimal.valueOf(150.0);

        // When
        paymentService.processPayment(payment1, totalAmount);

        // Then
        assertFalse(payment1.isSuccess());
        verify(paymentRepository, times(1)).save(payment1);
    }

    @Test
    void it_should_delete_payment_by_id() {
        // Given
        Long paymentId = 1L;

        // When
        paymentService.deletePayment(paymentId);

        // Then
        verify(paymentRepository, times(1)).delete(paymentId);
    }
}
