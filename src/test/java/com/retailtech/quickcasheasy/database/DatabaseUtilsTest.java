package com.retailtech.quickcasheasy.database;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseUtilsTest {

    private DatabaseUtils dbUtils;

    @BeforeAll
    void setup() {
        dbUtils = new DatabaseUtils();
        // Run initialization script to create tables if needed
        dbUtils.runScript("test.sql");
    }

    @Test
    @DisplayName("Test executeInsert - Insert a new row and retrieve its generated ID")
    void testExecuteInsert() {
        // Act: Insert a new row into the test table
        Long generatedId = dbUtils.executeInsert("INSERT INTO test (name) VALUES (?)", "TestName");

        // Assert: Check if the generated ID is not null
        assertNotNull(generatedId, "Generated ID should not be null after inserting");
    }

    @Test
    @DisplayName("Test executeUpdate - Update an existing row")
    void testExecuteUpdate() {
        // Arrange: Insert a row first to update
        Long generatedId = dbUtils.executeInsert("INSERT INTO test (name) VALUES (?)", "OldName");

        // Act: Update the name of the inserted row
        dbUtils.executeUpdate("UPDATE test SET name = ? WHERE id = ?", "UpdatedName", generatedId);

        // Assert: Check if the name has been updated
        String updatedName = dbUtils.executeQuery("SELECT name FROM test WHERE id = ?", rs -> {
            if (rs.next()) {
                return rs.getString("name");
            }
            return null;
        }, generatedId);

        assertEquals("UpdatedName", updatedName, "The name should be updated to 'UpdatedName'");
    }

    @Test
    @DisplayName("Test executeQuery - Retrieve data from the database")
    void testExecuteQuery() {
        // Arrange: Insert a row to be queried
        Long generatedId = dbUtils.executeInsert("INSERT INTO test (name) VALUES (?)", "QueryTestName");

        // Act: Query the inserted row by ID
        String name = dbUtils.executeQuery("SELECT name FROM test WHERE id = ?", rs -> {
            if (rs.next()) {
                return rs.getString("name");
            }
            return null;
        }, generatedId);

        // Assert: Check if the retrieved name matches the inserted name
        assertEquals("QueryTestName", name, "The name retrieved should match the inserted value");
    }

    @Test
    @DisplayName("Test runScript - Execute SQL script")
    void testRunScript() {
        // Act: Run the script to create a new table
        Executable runScriptExecutable = () -> dbUtils.runScript("test.sql");

        // Assert: No exception should be thrown
        assertDoesNotThrow(runScriptExecutable, "Running the init.sql script should not throw any exception");
    }

    @AfterAll
    void tearDown() {
        // Cleanup: Drop the test table after all tests
        dbUtils.executeUpdate("DROP TABLE IF EXISTS test");
    }
}