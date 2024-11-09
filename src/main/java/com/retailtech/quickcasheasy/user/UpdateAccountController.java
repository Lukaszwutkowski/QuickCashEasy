package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.user.dto.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class UpdateAccountController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    private UserDTO user;
    private boolean isUpdated = false;

    @FXML
    public void initialize() {
        // Initialization logic if needed
    }

    public void setUser(UserDTO user) {
        this.user = user;
        usernameField.setText(user.getUserName());
        passwordField.setText(user.getPassword());
    }

    @FXML
    private void handleSave() {
        // Update user details
        if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
            user.setUserName(usernameField.getText());
            user.setPassword(passwordField.getText());
            isUpdated = true;
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account updated successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
        }
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public UserDTO getUpdatedUser() {
        return user;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
