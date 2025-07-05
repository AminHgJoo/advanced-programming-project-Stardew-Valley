package com.client.utils;

import com.client.ClientApp;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.server.utilities.Response;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class HTTPUtil {
    /**
     * Returns null if the connection failed.
     *
     * @author AminHg
     */
    public static HttpResponse<JsonNode> post(String url, JsonObject req) {
        try {
            return Unirest.post(url)
                .header("Content-Type", "application/json")
                .header("Authorization", ClientApp.token)
                .body(req.toString())
                .asJson();
        } catch (Exception e) {
            return null;
        }
    }

    public static HttpResponse<JsonNode> get(String url) {
        try {
            return Unirest.get(url)
                .header("Content-Type", "application/json")
                .header("Authorization", ClientApp.token)
                .asJson();
        } catch (Exception e) {
            return null;
        }
    }

    public static Response deserializeHttpResponse(HttpResponse<JsonNode> response) {
        Gson gson = new Gson();
        String jsonStr = response.getBody().toString();
        return gson.fromJson(jsonStr, Response.class);
    }
}
