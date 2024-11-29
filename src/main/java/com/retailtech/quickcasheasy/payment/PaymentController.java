package com.retailtech.quickcasheasy.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

public class PaymentController {

    @FXML
    private Label totalAmountLabel;

    @FXML
    private TextField cardNumberField;

    @FXML
    private Button payButton;

    @FXML
    private TextField loginField;


    private BigDecimal amountToPay; // Total amount to pay
    private BankPaymentClient paymentClient;

    @FXML
    public void initialize() {
        paymentClient = new BankPaymentClient(); // Initialize the BankPaymentClient
        updateTotalAmount(); // Display the total amount in the UI
    }

    /**
     * Sets the total amount to be paid.
     *
     * @param amount the total amount
     */
    public void setAmountToPay(BigDecimal amount) {
        this.amountToPay = amount;
        updateTotalAmount();
    }

    /**
     * Updates the displayed total amount label.
     */
    private void updateTotalAmount() {
        if (totalAmountLabel != null && amountToPay != null) {
            totalAmountLabel.setText(String.format("%.2f NOK", amountToPay));
        }
    }

    /**
     * Handles the payment process when the "Pay Now" button is clicked.
     */
    @FXML
    private void handlePayment() {
        String cardNumber = cardNumberField.getText();

        if (cardNumber == null || cardNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter or scan a valid card number.");
            return;
        }

        try {
            int parsedCardNumber = Integer.parseInt(cardNumber);

            // Make the bank payment via the client
            String response = paymentClient.makeBankPayment(parsedCardNumber, amountToPay);

            // Show success or error message
            showAlert(Alert.AlertType.INFORMATION, "Payment Status", response);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Card number must be numeric.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to process payment: " + e.getMessage());
        }
    }

    /**
     * Displays an alert dialog.
     *
     * @param alertType the type of alert
     * @param title     the title of the alert dialog
     * @param message   the content message of the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
