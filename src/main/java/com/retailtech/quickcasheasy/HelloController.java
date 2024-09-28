package com.retailtech.quickcasheasy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import java.io.IOException;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick(ActionEvent event) {
        welcomeText.setText("Hello, World!");
    }
    @FXML
    private TabPane mainTabPane;

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/retailtech/quickcasheasy/user/user_view.fxml"));
            Parent userView = loader.load();
            Tab userTab = new Tab("User");
            userTab.setContent(userView);
            userTab.setClosable(false);
            mainTabPane.getTabs().add(userTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}