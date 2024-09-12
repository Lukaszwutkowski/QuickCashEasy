package com.retailtech.quickcasheasy.payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    List<Payment> findAll();

    Optional<Payment> findById(Long id);

    void save(Payment payment);

    void delete(Long id);
}
