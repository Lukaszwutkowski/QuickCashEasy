package com.retailtech.quickcasheasy.database;

import com.retailtech.quickcasheasy.QuickCashEasyApplication;
import javafx.application.Application;

/**
 * Initializes the SQLite database and runs SQL scripts for setup.
 */
public class DatabaseInitializer {
    public static void main(String[] args) {
        DatabaseUtils databaseUtils = new DatabaseUtils();

        // Run script to initialize the database
        try {
            databaseUtils.runScript("scripts/init.sql");
            System.out.println("Database initialization script executed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error running initialization script.");
        }

        // Launch JavaFX application
        Application.launch(QuickCashEasyApplication.class, args);
    }
}
