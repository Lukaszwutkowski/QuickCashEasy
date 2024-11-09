package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.user.dto.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller for the edit user dialog window.
 */
public class EditUserController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;

    private UserDTO user;
    private boolean isSaved = false;

    /**
     * Initialize the fields with the selected user's information.
     */
    public void setUser(UserDTO user) {
        this.user = user;
        usernameField.setText(user.getUserName());
        roleComboBox.setValue(user.getRole().toString());
    }

    /**
     * Returns whether the user clicked save or not.
     *
     * @return true if saved, false otherwise
     */
    public boolean isSaved() {
        return isSaved;
    }

    /**
     * Handles saving the user.
     */
    @FXML
    private void handleSave() {
        // Update the user object with new values
        user.setUserName(usernameField.getText());
        user.setPassword(passwordField.getText());
        user.setRole(UserRole.valueOf(roleComboBox.getValue()));

        // Indicate that the user has saved the changes
        isSaved = true;

        // Close the window
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles canceling the edit and closes the window.
     */
    @FXML
    private void handleCancel() {
        // Close the window without saving
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
