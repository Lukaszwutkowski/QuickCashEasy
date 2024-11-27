package com.retailtech.quickcasheasy.payment;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Client for handling communication with the bank's payment API.
 * This class is responsible for sending payment requests to the bank server.
 */
public class BankPaymentClient {

    // URL of the bank's payment API
    private static final String BANK_PAYMENT_URL = "http://localhost:8080/api/payment/make";

    // HttpClient for sending HTTP requests
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Sends a payment request to the bank server.
     *
     * @param cardNumber the card number associated with the primary account to be charged
     * @param amount     the amount to be deducted from the account
     * @return a message indicating the payment status
     */
    public String makeBankPayment(int cardNumber, BigDecimal amount) throws IOException, InterruptedException {

        try {
            // Construct the URL with encoded parameters for the payment request
            String url = String.format("%s?cardNumber=%d&amount=%s",
                    BANK_PAYMENT_URL,
                    cardNumber,
                    URLEncoder.encode(amount.toString(), StandardCharsets.UTF_8));

            // Create a POST request to the bank's payment endpoint
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            // Send the request and receive the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Return the appropriate message based on the response status
            if (response.statusCode() == 200) {
                return "Payment processed successfully.";
            } else {
                return "Payment failed: " + response.body();
            }
        } catch (Exception e) {
            return "Error processing payment: " + e.getMessage();
        }
    }
}
