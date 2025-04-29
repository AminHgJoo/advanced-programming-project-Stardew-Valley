package com.example.utilities;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class HuggingFaceChat {
    private static final String API_URL = "https://api-inference.huggingface.co/models/facebook/blenderbot-400M-distill";
    private static final String API_KEY = System.getProperty("AI_API_KEY"); // Replace with your token

    public static String getAIResponse(String userMessage) throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("inputs", userMessage);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONArray jsonResponse = new JSONArray(response.body());
        return jsonResponse.getJSONObject(0).getString("generated_text");
    }

    public static String getNpcDialogue(String message) {
        try {
            String response = getAIResponse(message);
            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Fuck You";
        }
    }
}