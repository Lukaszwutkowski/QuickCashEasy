package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.database.DatabaseConnectionManager;
import com.retailtech.quickcasheasy.user.dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller class for handling user login in the QuickCashEasy application.
 */
public class LoginController {

    // FXML-injected UI components for login
    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    // Facade for user-related business logic
    private UserFacade userFacade;

    /**
     * Default constructor for LoginController.
     */
    public LoginController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize UserFacade with required dependencies
        this.userFacade = new UserFacade(new UserService(new UserRepositoryImpl()));
    }

    /**
     * Handles user login.
     * Validates the input fields and calls the userFacade to authenticate the user.
     * If successful, navigates to the appropriate user view based on the role.
     */
    @FXML
    private void handleLogin() {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        // Authenticate user using database connection
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                UserDTO loggedInUser = new UserDTO(username, role);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Logged in successfully.");
                loginUsernameField.clear();
                loginPasswordField.clear();

                // Navigate to a different view based on the role
                switchToPostLoginView(loggedInUser);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid login credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while connecting to the database.");
        }
    }

    /**
     * Switches to the appropriate view based on the user's role.
     *
     * @param user The logged-in user whose role determines the view to load.
     */
    private void switchToPostLoginView(UserDTO user) {
        try {
            FXMLLoader loader;
            if (user.getRole().equals("ADMIN")) {
                loader = new FXMLLoader(getClass().getResource("/com/retailtech/quickcasheasy/user/admin_view.fxml"));
            } else if (user.getRole().equals("CASHIER")) {
                loader = new FXMLLoader(getClass().getResource("/com/retailtech/quickcasheasy/user/cashier_view.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/com/retailtech/quickcasheasy/user/customer_view.fxml"));
            }

            Parent root = loader.load();
            Stage stage = (Stage) loginUsernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the user dashboard.");
        }
    }

    /**
     * Utility method to display alerts to the user.
     *
     * @param type    The type of alert (e.g., ERROR, INFORMATION).
     * @param title   The title of the alert dialog.
     * @param message The content message of the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}