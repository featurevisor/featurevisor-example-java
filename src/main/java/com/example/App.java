package com.example;

import com.featurevisor.sdk.Instance;
import com.featurevisor.types.DatafileContent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class App {
    public static void main(String[] args) {
        try {
            // Fetch JSON content from the Featurevisor datafile URL
            String jsonContent = fetchJsonFromUrl("https://featurevisor-example-cloudflare.pages.dev/production/featurevisor-tag-all.json");
            System.out.println("Fetched JSON content");

            // Parse JSON string to DatafileContent
            DatafileContent datafileContent = DatafileContent.fromJson(jsonContent);
            System.out.println("Parsed DatafileContent successfully");

            // Create Featurevisor SDK instance with the datafile content
            Instance instance = new Instance(
                new Instance.InstanceOptions()
                    .datafile(datafileContent)
            );

            // evaluate feature
            boolean isFeatureEnabled = instance.isEnabled("my_feature");
            System.out.println("Feature is enabled: " + isFeatureEnabled);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String fetchJsonFromUrl(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch JSON: HTTP " + response.statusCode());
        }

        return response.body();
    }
}
