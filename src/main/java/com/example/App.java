package com.example;

import com.featurevisor.sdk.Featurevisor;
import com.featurevisor.sdk.FeaturevisorLogLevel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class App {
    private static final String DATAFILE_URL =
            "https://featurevisor-example-cloudflare.pages.dev/production/featurevisor-sdk-v3.json";

    public static void main(String[] args) throws Exception {
        String datafile = fetchDatafile();
        Map<String, Object> context = Map.of(
                "userId", "customer-123",
                "country", "nl",
                "locale", "nl-NL",
                "accountPlan", "pro"
        );

        Featurevisor f = Featurevisor.createFeaturevisor(
                new Featurevisor.FeaturevisorOptions()
                        .datafileString(datafile)
                        .context(context)
                        .logLevel(FeaturevisorLogLevel.ERROR)
        );

        try {
            boolean commerceEnabled = f.isEnabled("commerce_platform");
            String checkoutVariation = f.getVariation("checkout_experience");
            Integer maxItems = f.getVariableInteger("checkout_experience", "max_items");
            List<String> paymentMethods = f.getVariableArray(
                    "checkout_experience",
                    "payment_methods"
            );
            Map<String, Object> endpoints = f.getVariableObject("serviceEndpoints");
            String supportContact = f.getVariableString("supportContact");

            System.out.println("Commerce platform enabled: " + commerceEnabled);
            System.out.println("Checkout variation: " + valueOrUnavailable(checkoutVariation));
            System.out.println("Maximum checkout items: " + valueOrUnavailable(maxItems));
            System.out.println("Payment methods: " + paymentMethods);
            System.out.printf(
                    "Service endpoint: %s (timeout: %s ms, retries: %s)%n",
                    endpoints.get("baseUrl"),
                    endpoints.get("timeoutMs"),
                    endpoints.get("retries")
            );
            System.out.println("Support contact: " + valueOrUnavailable(supportContact));
        } finally {
            f.close();
        }
    }

    private static String fetchDatafile() throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DATAFILE_URL))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException(
                    "Datafile request failed with HTTP " + response.statusCode()
            );
        }
        return response.body();
    }

    private static Object valueOrUnavailable(Object value) {
        return value == null ? "unavailable" : value;
    }
}
