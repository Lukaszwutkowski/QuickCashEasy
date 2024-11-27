package com.retailtech.quickcasheasy.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.math.BigDecimal;

public class PaymentController {

    @FXML
    private Label amountLabel;

    @FXML
    private TextField cardNumberField;

    @FXML
    private Button payButton;

    private BigDecimal amountToPay;

    private PaymentFacade paymentFacade;

    public PaymentController() {
        // Initialize with a dummy amount for testing; this can be set dynamically
        this.amountToPay = new BigDecimal("100.00");
        this.paymentFacade = new PaymentFacade(new PaymentService());
    }

    @FXML
    public void initialize() {
        amountLabel.setText(amountToPay.toString());
    }

    /**
     * Handles the payment process.
     * Gets the card number from the input field and attempts to process the payment.
     */
    @FXML
    private void handlePayment() {
        String cardNumberText = cardNumberField.getText();

        // Validate card number input
        if (cardNumberText == null || cardNumberText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter or scan a valid card number.");
            return;
        }

        try {
            int cardNumber = Integer.parseInt(cardNumberText);

            // Call the facade to process the payment
            String result = paymentFacade.processPayment(
                    null, // Pass a generated or predefined transaction ID
                    amountToPay,
                    "Card Payment",
                    cardNumber
            );

            showAlert(Alert.AlertType.INFORMATION, "Payment Status", result);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid card number format. Please enter a numeric card number.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Payment Error", "Failed to process payment: " + e.getMessage());
        }
    }

    /**
     * Displays an alert dialog to the user.
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

    /**
     * Sets the amount to be paid, which will be displayed in the window.
     *
     * @param amount the amount to set
     */
    public void setAmountToPay(BigDecimal amount) {
        this.amountToPay = amount;
        amountLabel.setText(amount.toString());
    }
}
