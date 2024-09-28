package com.retailtech.quickcasheasy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class QuickCashEasyApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Załaduj główny widok aplikacji z pliku FXML
        FXMLLoader fxmlLoader = new FXMLLoader(QuickCashEasyApplication.class.getResource("/com/retailtech/quickcasheasy/user/user_view.fxml"));

        // Utwórz scenę z załadowanym widokiem
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // Ustaw tytuł okna
        primaryStage.setTitle("QuickCashEasy - Nowa klasa startowa");

        // Ustaw scenę i pokaż okno
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Uruchom aplikację
        launch(args);
    }
}
