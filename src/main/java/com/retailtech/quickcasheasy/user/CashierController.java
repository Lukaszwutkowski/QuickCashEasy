package com.retailtech.quickcasheasy.user;

import com.retailtech.quickcasheasy.cart.dto.CartItemDTO;
import com.retailtech.quickcasheasy.payment.PaymentController;
import com.retailtech.quickcasheasy.product.ProductRepositoryImpl;
import com.retailtech.quickcasheasy.product.dto.ProductDTO;
import com.retailtech.quickcasheasy.user.dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Controller class for handling cashier-related operations in the JavaFX application.
 * Manages viewing cart items, scanning products, updating account information, and logging out.
 */
public class CashierController {

    @FXML
    private Text totalAmountText;

    @FXML
    private TableView<CartItemDTO> cartTableView;

    @FXML
    private TableColumn<CartItemDTO, String> productNameColumn;

    @FXML
    private TableColumn<CartItemDTO, Integer> quantityColumn;

    @FXML
    private TableColumn<CartItemDTO, BigDecimal> totalPriceColumn;

    @FXML
    private TextField barcodeField;

    // Observable list for table data binding
    private ObservableList<CartItemDTO> cartItemList;

    // Currently logged-in user
    private UserDTO loggedInUser;

    // Product repository for handling product-related operations
    private ProductRepositoryImpl productRepository;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize the observable list
        this.cartItemList = FXCollections.observableArrayList();

        // Initialize the product repository
        this.productRepository = new ProductRepositoryImpl();

        // Configure the TableView columns
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Add custom cell factory for formatting BigDecimal in totalPriceColumn
        totalPriceColumn.setCellFactory(column -> new TableCell<CartItemDTO, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " NOK");
                }
            }
        });

        // Load cart items into the table
        loadCartItems();

        // Listen for Enter key on barcodeField
        barcodeField.setOnAction(this::handleAddToCart);
    }

    /**
     * Handles viewing cart items for the logged-in customer.
     */
    @FXML
    private void handleViewCart(ActionEvent actionEvent) {
        loadCartItems();
    }

    /**
     * Loads all cart items for the logged-in user and displays them in the TableView.
     */
    private void loadCartItems() {
        try {
            // Here we should load cart items for the logged-in user
            // Placeholder implementation as the actual cart retrieval logic is not provided
            // cartItemList.setAll(cartService.getCartItemsByUser(loggedInUser.getId()));
            cartTableView.setItems(cartItemList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load cart items.");
        }
    }

    /**
     * Handles the action of proceeding to the payment screen.
     * Opens a new window for payment, passing the total amount to be paid.
     */
    @FXML
    private void handleProceedToPayment(ActionEvent actionEvent) {
        try {
            // Load the payment window FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/retailtech/quickcasheasy/payment/payment_view.fxml"));
            Parent root = loader.load();

            // Get the controller for the payment window
            PaymentController paymentController = loader.getController();

            // Calculate total amount from the cart items
            BigDecimal totalAmount = calculateTotalAmount();
            paymentController.setAmountToPay(totalAmount);  // Pass total amount to payment controller

            // Set up the stage for the payment window
            Stage stage = new Stage();
            stage.setTitle("Payment");
            stage.setScene(new Scene(root));
            stage.showAndWait();  // Wait for the payment window to close before returning

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Payment window.");
            e.printStackTrace();
        }
    }

    /**
     * Calculates the total amount based on items in the cart.
     *
     * @return The total amount as BigDecimal.
     */
    private BigDecimal calculateTotalAmount() {
        BigDecimal total = cartItemList.stream()
                .map(CartItemDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Update the totalAmountText in the UI
        totalAmountText.setText(total.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " NOK");

        return total;
    }

    /**
     * Handles adding a product to the cart by scanning the barcode.
     */
    @FXML
    private void handleAddToCart(ActionEvent actionEvent) {
        String barcode = barcodeField.getText();

        if (barcode == null || barcode.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid barcode.");
            return;
        }

        Optional<ProductDTO> productOpt = productRepository.getProductByBarcode(barcode).map(product -> new ProductDTO(product.getBarcode(), product.getName(), product.getPrice(), product.getCategoryId()));

        if (productOpt.isPresent()) {
            ProductDTO product = productOpt.get();
            boolean itemExists = false;

            // Check if item is already in the cart and update quantity
            for (CartItemDTO item : cartItemList) {
                if (item.getProductName().equals(product.getName())) {
                    itemExists = true;
                    cartItemList.set(cartItemList.indexOf(item),
                            new CartItemDTO(item.getProductName(), item.getQuantity() + 1,
                                    item.getTotalPrice().add(product.getPrice())));
                    break;
                }
            }

            // If item does not exist in the cart, add it as a new item
            if (!itemExists) {
                cartItemList.add(new CartItemDTO(product.getName(), 1, product.getPrice()));
            }

            cartTableView.refresh();
            barcodeField.clear();
            calculateTotalAmount();

        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Product not found.");
        }
    }

    /**
     * Handles updating the account information of the logged-in customer.
     * Opens a new window to allow the user to edit their account details.
     * If the user saves the changes, the loggedInUser object is updated.
     *
     * @param actionEvent the ActionEvent triggered by the button click
     */
    @FXML
    private void handleUpdateAccount(ActionEvent actionEvent) {
        if (loggedInUser == null) {
            System.err.println("Error: loggedInUser is null before setting in EditUserController.");
            return; // Handle the error or set a default user if needed
        }

        try {
            // Load the FXML file for the edit user view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/retailtech/quickcasheasy/user/edit_user_view.fxml"));
            Parent root = loader.load();

            // Get the controller from the FXML loader and set the logged-in user
            EditUserController controller = loader.getController();
            controller.setUser(loggedInUser);

            // Create a new stage for the edit user window
            Stage stage = new Stage();
            stage.setTitle("Update Account");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Check if changes were saved in the EditUserController
            if (controller.isSaved()) {
                // Retrieve updated user information
                loggedInUser = controller.getUser();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account updated successfully.");
            }

        } catch (IOException e) {
            // Display an error alert if the view could not be loaded
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Update Account view.");
            e.printStackTrace();
        }
    }


    /**
     * Handles user logout and returns to the login screen.
     */
    @FXML
    private void handleLogout(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/retailtech/quickcasheasy/home/home_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) cartTableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to log out.");
            e.printStackTrace();
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

    /**
     * Sets the logged-in user for this controller.
     *
     * @param user The logged-in user.
     */
    public void setLoggedInUser(UserDTO user) {
        this.loggedInUser = user;
    }
}
