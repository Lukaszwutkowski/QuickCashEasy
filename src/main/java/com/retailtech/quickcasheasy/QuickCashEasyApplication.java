package com.retailtech.quickcasheasy;

import com.retailtech.quickcasheasy.database.DatabaseUtils; // Import DatabaseUtils
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the QuickCashEasy JavaFX application.
 * This class is responsible for launching the JavaFX application and initializing the necessary resources.
 */
public class QuickCashEasyApplication extends Application {

    /**
     * The main entry point for JavaFX applications.
     * This method is called after the JavaFX application is launched.
     *
     * @param primaryStage The primary stage for this application, onto which
     *                     the application scene can be set.
     * @throws IOException If an I/O error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the main application view from FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(QuickCashEasyApplication.class.getResource("/com/retailtech/quickcasheasy/home/home_view.fxml"));

        // Create the scene with the loaded view
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // Set the window title
        primaryStage.setTitle("QuickCashEasy");

        // Set the scene and show the window
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main method to start the QuickCashEasy application.
     * This method initializes the database utility class to ensure that the H2 driver is loaded
     * before launching the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        // Initialize DatabaseUtils to ensure H2 driver is loaded
        System.out.println("Initializing DatabaseUtils...");
        DatabaseUtils dbUtils = new DatabaseUtils(); // Forces loading of the DatabaseUtils class

        // Run initialization script to create tables and insert data
        System.out.println("Running initialization script...");
        dbUtils.runScript("init.sql");

        // Log to confirm that the driver has been loaded
        System.out.println("DatabaseUtils initialized. Starting JavaFX application...");

        // Run the application
        launch(args);
    }
}