package com.retailtech.quickcasheasy.payment;

import com.retailtech.quickcasheasy.database.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class PaymentRepositoryImpl implements PaymentRepository {

    private final DatabaseUtils dbUtils; // Utility class for managing database connections

    // Constructor injecting DatabaseUtils
    public PaymentRepositoryImpl(DatabaseUtils dbUtils) {
        this.dbUtils = dbUtils;
        initializeDatabase(); // Initialize the payments table if it doesn't exist
    }

    // Initialize the payments table in the database if it doesn't exist
    private void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS payments ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "method VARCHAR(255), "
                + "amount DECIMAL(10,2), "
                + "status VARCHAR(50), "
                + "success BOOLEAN)";
        dbUtils.executeUpdate(sql);
    }

    // Fetch all payments from the database
    @Override
    public List<Payment> findAll() {
        String sql = "SELECT * FROM payments";
        return dbUtils.executeQuery(sql, this::mapResultSetToPaymentList);
    }

    // Fetch a payment by its ID from the database
    @Override
    public Optional<Payment> findById(Long id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        return dbUtils.executeQuery(sql, rs -> {
            try {
                if (rs.next()) {
                    return Optional.of(mapRowToPayment(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return Optional.empty();
        }, id);
    }

    // Save a payment to the database
    @Override
    public void save(Payment payment) {
        if (payment.getId() == null) {
            // Insert new payment
            String sql = "INSERT INTO payments (method, amount, status, success) VALUES (?, ?, ?, ?)";
            Long generatedId = dbUtils.executeInsert(sql, payment.getMethod(), payment.getAmount(), payment.getStatus(), payment.isSuccess());
            payment.setId(generatedId);  // Set the generated ID back to the payment object
        } else {
            // Update existing payment
            String sql = "UPDATE payments SET method = ?, amount = ?, status = ?, success = ? WHERE id = ?";
            dbUtils.executeUpdate(sql, payment.getMethod(), payment.getAmount(), payment.getStatus(), payment.isSuccess(), payment.getId());
        }
    }

    // Delete a payment by its ID from the database
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM payments WHERE id = ?";
        dbUtils.executeUpdate(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT 1 FROM payments WHERE id = ?";
        return dbUtils.executeQuery(sql, rs -> {
            try {
                return rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, id);
    }

    // Helper method to map a result set to a list of payments
    private List<Payment> mapResultSetToPaymentList(ResultSet rs) {
        List<Payment> payments = new ArrayList<>();
        try {
            while (rs.next()) {
                payments.add(mapRowToPayment(rs)); // Map each row to a Payment object
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping result set to payment list", e);
        }
        return payments;
    }

    // Helper method to map a single row in the result set to a Payment object
    private Payment mapRowToPayment(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getLong("id"),
                rs.getBigDecimal("amount"),
                rs.getString("method"),
                rs.getString("status"),
                rs.getBoolean("success")
        );
    }

}
