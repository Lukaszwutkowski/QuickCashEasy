package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.user.dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import com.retailtech.quickcasheasy.exception.UserNotFoundException;
import javafx.scene.layout.HBox;

import java.util.List;

public class UserController {


    @FXML
    private TextField regUsernameField;

    @FXML
    private PasswordField regPasswordField;

    @FXML
    private ComboBox<String> regRoleComboBox;

    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

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

    private UserFacade userFacade;

    private ObservableList<UserDTO> userList;

    public UserController() {
        this.userFacade = new UserFacade(new UserService(new UserRepositoryImpl()));
        this.userList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {

        regRoleComboBox.setItems(FXCollections.observableArrayList("USER", "ADMIN"));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        addActionsToTable();

        loadUsers();
    }

    @FXML
    private void handleRegister() {
        String username = regUsernameField.getText();
        String password = regPasswordField.getText();
        String roleStr = regRoleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || roleStr == null) {
            showAlert(AlertType.ERROR, "Validation Error", "Wszystkie pola są wymagane.");
            return;
        }

        UserRole role;
        try {
            role = UserRole.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Validation Error", "Nieprawidłowa rola użytkownika.");
            return;
        }

        try {
            userFacade.registerUser(username, password, role);
            showAlert(AlertType.INFORMATION, "Success", "Użytkownik zarejestrowany pomyślnie.");
            regUsernameField.clear();
            regPasswordField.clear();
            regRoleComboBox.getSelectionModel().clearSelection();
            loadUsers();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Registration Failed", e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Wszystkie pola są wymagane.");
            return;
        }

        boolean isAuthenticated = userFacade.authenticateUser(username, password);
        if (isAuthenticated) {
            showAlert(AlertType.INFORMATION, "Success", "Zalogowano pomyślnie.");
            loginUsernameField.clear();
            loginPasswordField.clear();
            // Możesz tutaj przełączyć widok na panel użytkownika lub admina
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Nieprawidłowe dane logowania.");
        }
    }

    private void loadUsers() {
        try {
            List<UserDTO> users = userFacade.getAllUsers();
            userList.setAll(users);
            userTableView.setItems(userList);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Nie udało się załadować użytkowników.");
        }
    }

    private void addActionsToTable() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edytuj");
            private final Button deleteButton = new Button("Usuń");
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

    private void handleEditUser(UserDTO user) {
        showAlert(AlertType.INFORMATION, "Edit User", "Funkcja edycji nie jest jeszcze zaimplementowana.");
    }

    private void handleDeleteUser(UserDTO user) {
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Czy na pewno chcesz usunąć użytkownika: " + user.getUserName() + "?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                userFacade.deleteUser(user.getUserName());
                showAlert(AlertType.INFORMATION, "Success", "Użytkownik usunięty pomyślnie.");
                loadUsers();
            } catch (UserNotFoundException e) {
                showAlert(AlertType.ERROR, "Error", "Użytkownik nie został znaleziony.");
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Nie udało się usunąć użytkownika.");
            }
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
