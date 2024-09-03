package com.retailtech.quickcasheasy.barcode;

public class BarcodeScannerFacade {
    private final BarcodeScanner barcodeScanner;

    public BarcodeScannerFacade() {
        this.barcodeScanner = new BarcodeScanner();
        barcodeScanner.initializeScanner();
    }

    public String scanBarcode() {
        return barcodeScanner.scan();
    }
}
