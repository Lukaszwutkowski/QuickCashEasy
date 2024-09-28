package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.exception.UserNotFoundException;
import com.retailtech.quickcasheasy.user.dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * Controller class for handling user-related operations in the JavaFX application.
 * Manages user registration, login, and user management functionalities.
 */
public class UserController {

    // FXML-injected UI components for registration
    @FXML
    private TextField regUsernameField;

    @FXML
    private PasswordField regPasswordField;

    @FXML
    private ComboBox<String> regRoleComboBox;

    // FXML-injected UI components for login
    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

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

    /**
     * Constructor for UserController.
     * Initializes the UserFacade and the observable list for users.
     * Note: In JavaFX, it's recommended to initialize such dependencies in the initialize() method.
     */
    public UserController() {
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
     * Handles the registration of a new user.
     * Validates input fields and uses the UserFacade to register the user.
     */
    @FXML
    private void handleRegister() {
        String username = regUsernameField.getText();
        String password = regPasswordField.getText();
        String roleStr = regRoleComboBox.getValue();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty() || roleStr == null) {
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

        try {
            // Register the user using the facade
            userFacade.registerUser(username, password, role);
            showAlert(AlertType.INFORMATION, "Success", "User registered successfully.");

            // Clear input fields
            regUsernameField.clear();
            regPasswordField.clear();
            regRoleComboBox.getSelectionModel().clearSelection();

            // Refresh the user table
            loadUsers();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Registration Failed", e.getMessage());
        }
    }

    /**
     * Handles user login.
     * Validates input fields and uses the UserFacade to authenticate the user.
     */
    @FXML
    private void handleLogin() {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        boolean isAuthenticated = userFacade.authenticateUser(username, password);
        if (isAuthenticated) {
            showAlert(AlertType.INFORMATION, "Success", "Logged in successfully.");

            // Clear input fields
            loginUsernameField.clear();
            loginPasswordField.clear();

            // TODO: Switch view to user or admin panel based on role
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid login credentials.");
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
                // Handle Edit button action
                editButton.setOnAction(event -> {
                    UserDTO user = getTableView().getItems().get(getIndex());
                    handleEditUser(user);
                });

                // Handle Delete button action
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
     * Currently, this functionality is not implemented.
     *
     * @param user The user to edit.
     */
    private void handleEditUser(UserDTO user) {
        showAlert(AlertType.INFORMATION, "Edit User", "Edit functionality is not implemented yet.");
        // TODO: Implement user editing functionality
    }

    /**
     * Handles the deletion of a user.
     * Confirms the action with the user before deleting.
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
     * Utility method to display alerts to the user.
     *
     * @param type    The type of alert.
     * @param title   The title of the alert dialog.
     * @param message The message to display.
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
