package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.exception.UserNotFoundException;
import com.retailtech.quickcasheasy.user.dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for handling user-related operations in the JavaFX application.
 * Manages user registration, login, and user management functionalities.
 */
public class AdminController {

    // FXML-injected UI components for registration
    @FXML
    private TextField regUsernameField;

    @FXML
    private PasswordField regPasswordField;

    @FXML
    private ComboBox<String> regRoleComboBox;

    // FXML-injected UI components for user management table
    @FXML
    private TableView<UserDTO> userTableView;

    @FXML
    private TableColumn<UserDTO, Long> idColumn;

    @FXML
    private TableColumn<UserDTO, String> usernameColumn;

    @FXML
    private TableColumn<UserDTO, String> roleColumn;

    @FXML
    private TableColumn<UserDTO, Void> actionsColumn;

    // Facade for user-related business logic
    private UserFacade userFacade;

    // Observable list for table data binding
    private ObservableList<UserDTO> userList;

    // Currently selected user for editing
    private UserDTO selectedUser;

    /**
     * Constructor for AdminController.
     * Initializes the UserFacade and the observable list for users.
     */
    public AdminController() {
        // Initialization will be moved to the initialize() method
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize UserFacade with required dependencies
        this.userFacade = new UserFacade(new UserService(new UserRepositoryImpl()));

        // Initialize the observable list
        this.userList = FXCollections.observableArrayList();

        // Set items for the role ComboBox
        regRoleComboBox.setItems(FXCollections.observableArrayList("CASHIER", "CUSTOMER", "ADMIN"));

        // Configure the TableView columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Add action buttons to the table
        addActionsToTable();

        // Load users into the table
        loadUsers();
    }

    /**
     * Handles the registration or updating of a user.
     */
    @FXML
    private void handleRegister() {
        String username = regUsernameField.getText();
        String password = regPasswordField.getText();
        String roleStr = regRoleComboBox.getValue();

        // Validate input fields
        if (username.isEmpty() || (selectedUser == null && password.isEmpty()) || roleStr == null) {
            showAlert(AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        UserRole role;
        try {
            role = UserRole.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Validation Error", "Invalid user role.");
            return;
        }

        if (selectedUser == null) {
            // Register the user
            try {
                userFacade.registerUser(null, username, password, role);
                showAlert(AlertType.INFORMATION, "Success", "User registered successfully.");
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Registration Failed", e.getMessage());
            }
        } else {
            // Update the user
            userFacade.updateUser(selectedUser.getId(), username, password.isEmpty() ? selectedUser.getPassword() : password, role);
            showAlert(AlertType.INFORMATION, "Success", "User updated successfully.");
            selectedUser = null; // Reset after editing
        }

        // Clear fields and refresh the table
        clearFields();
        loadUsers();
    }

    /**
     * Handles the management of users, switching to the user management view.
     */
    @FXML
    private void handleManageUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/retailtech/quickcasheasy/user/user_management_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Stage) loader.getController()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load User Management view.");
        }
    }

    /**
     * Handles viewing reports, switching to the reports view.
     */
    @FXML
    private void handleViewReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/retailtech/quickcasheasy/reports/reports_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Stage) loader.getController()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load Reports view.");
        }
    }

    /**
     * Handles user logout and returns to the login screen.
     */
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/retailtech/quickcasheasy/home/home_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) userTableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to log out.");
            e.printStackTrace();
        }
    }

    /**
     * Loads all users from the database and displays them in the TableView.
     */
    private void loadUsers() {
        try {
            List<UserDTO> users = userFacade.getAllUsers();
            userList.setAll(users);
            userTableView.setItems(userList);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Failed to load users.");
        }
    }

    /**
     * Adds action buttons (Edit and Delete) to each row in the user TableView.
     */
    private void addActionsToTable() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox pane = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    UserDTO user = getTableView().getItems().get(getIndex());
                    handleEditUser(user);
                });

                deleteButton.setOnAction(event -> {
                    UserDTO user = getTableView().getItems().get(getIndex());
                    handleDeleteUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    /**
     * Handles the editing of a user.
     *
     * @param user The user to edit.
     */
    private void handleEditUser(UserDTO user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/retailtech/quickcasheasy/user/edit_user_view.fxml"));
            Parent root = loader.load();

            EditUserController controller = loader.getController();
            controller.setUser(user);

            Stage stage = new Stage();
            stage.setTitle("Edit User");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSaved()) {
                userFacade.updateUser(user.getId(), user.getUserName(), user.getPassword(), user.getRole());
                loadUsers();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the deletion of a user.
     *
     * @param user The user to delete.
     */
    private void handleDeleteUser(UserDTO user) {
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete user: " + user.getUserName() + "?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                userFacade.deleteUser(user.getUserName());
                showAlert(AlertType.INFORMATION, "Success", "User deleted successfully.");
                loadUsers();
            } catch (UserNotFoundException e) {
                showAlert(AlertType.ERROR, "Error", "User not found.");
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Failed to delete user.");
            }
        }
    }

    /**
     * Clears the input fields after registering or editing a user.
     */
    private void clearFields() {
        regUsernameField.clear();
        regPasswordField.clear();
        regRoleComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Utility method to display alerts to the user.
     *
     * @param type    The type of alert (e.g., ERROR, INFORMATION).
     * @param title   The title of the alert dialog.
     * @param message The content message of the alert.
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
