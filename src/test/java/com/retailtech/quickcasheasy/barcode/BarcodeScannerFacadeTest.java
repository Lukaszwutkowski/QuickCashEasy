package com.retailtech.quickcasheasy.barcode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BarcodeScannerFacadeTest {

    private BarcodeScannerFacade barcodeScannerFacade;

    @BeforeEach
    void setUp() {
        barcodeScannerFacade = new BarcodeScannerFacade();
    }

    @Test
    void it_should_be_able_to_scan_the_barcode_via_BarcodeScannerFacade() {
        // Given
        // When
        String barcode = barcodeScannerFacade.scanBarcode();

        // Then
        assertNotNull(barcode);
        assertEquals("barcode12345", barcode);
    }
}
