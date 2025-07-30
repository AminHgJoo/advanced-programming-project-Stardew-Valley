package com.server.utilities;

import com.common.models.NPCModels.NPC;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AIChat {
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = System.getProperty("AI_API_KEY"); // Replace with your token

    public static String getAIResponse(String userMessage , String context) throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("model", "deepseek/deepseek-chat-v3-0324:free");
        JSONArray arr = new JSONArray();
        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", context);
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        arr.put(0, userMsg);
        payload.put("messages", arr);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Authorization", "Bearer " + API_KEY)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse.getJSONArray("choices")
            .getJSONObject(0).getJSONObject("message").getString("content");
    }

    public static String getNpcDialogue(String message ,String ctx) {
        try {
            String response = getAIResponse(message , ctx);
            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Fuck You";
        }
    }
}
