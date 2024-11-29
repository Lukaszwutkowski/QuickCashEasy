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

    // Base URL of the bank's payment API
    private static final String BANK_PAYMENT_URL = "http://localhost:8080/api/payment/make";

    // HttpClient for sending HTTP requests
    private final HttpClient httpClient;

    /**
     * Constructs a new BankPaymentClient with a default HTTP client.
     */
    public BankPaymentClient() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL) // Enable redirect handling
                .build();
    }

    /**
     * Constructs a new BankPaymentClient with a custom HTTP client (useful for testing or specific configurations).
     *
     * @param httpClient the HttpClient to use
     */
    public BankPaymentClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Sends a payment request to the bank server.
     *
     * @param cardNumber the card number associated with the primary account to be charged
     * @param amount     the amount to be deducted from the account
     * @return a message indicating the payment status
     * @throws IOException          if there is an issue with network communication
     * @throws InterruptedException if the request is interrupted
     */
    public String makeBankPayment(int cardNumber, BigDecimal amount) throws IOException, InterruptedException {
        try {
            // Construct the URL with encoded parameters
            String url = String.format("%s?cardNumber=%d&amount=%s",
                    BANK_PAYMENT_URL,
                    cardNumber,
                    URLEncoder.encode(amount.toString(), StandardCharsets.UTF_8));

            // Create the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            // Send the request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Log redirect headers
            if (response.statusCode() == 302 || response.statusCode() == 301) {
                System.out.println("Redirected to: " + response.headers().firstValue("Location").orElse("Unknown Location"));
            }

            // Return processed response
            return processResponse(response);

        } catch (IOException | InterruptedException e) {
            throw e; // Re-throw for caller handling
        } catch (Exception e) {
            return "Unexpected error occurred during payment: " + e.getMessage();
        }
    }



    /**
     * Processes the HTTP response from the payment API.
     *
     * @param response the HttpResponse object
     * @return a user-friendly message based on the response
     */
    private String processResponse(HttpResponse<String> response) {
        switch (response.statusCode()) {
            case 200:
                return "Payment processed successfully.";
            case 302:
                return "Redirect occurred during payment. Check logs for the new location.";
            case 400:
                return "Payment failed: Invalid input. " + response.body();
            case 404:
                return "Payment failed: Endpoint not found.";
            case 500:
                return "Payment failed: Internal server error.";
            default:
                return String.format("Payment failed with unexpected status code: %d. Response: %s",
                        response.statusCode(), response.body());
        }
    }

}
