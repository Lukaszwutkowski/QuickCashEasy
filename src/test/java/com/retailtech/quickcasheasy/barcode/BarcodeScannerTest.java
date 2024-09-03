package com.retailtech.quickcasheasy.barcode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BarcodeScannerTest {

    private BarcodeScanner barcodeScanner;

    @BeforeEach
    void setUp() {
        barcodeScanner = new BarcodeScanner();
    }

    @Test
    void it_should_initialize_barcode_scanner() {
        // Given
        // When
        barcodeScanner.initializeScanner();

        // Then
        assertDoesNotThrow(() -> barcodeScanner.initializeScanner());
    }

    @Test
    void it_should_scan_and_return_barcode() {
        // Given
        barcodeScanner.initializeScanner();

        // When
        String barcode = barcodeScanner.scan();

        // Then
        assertNotNull(barcode);
        assertEquals("barcode12345", barcode);
    }
}
