package com.client.utils;

import com.client.ClientApp;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.server.utilities.Response;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.io.File;

public class HTTPUtil {
    /**
     * Returns null if the connection failed.
     *
     * @author AminHg
     */
    public static HttpResponse<JsonNode> post(String url, JsonObject req) {
        url = "http://" + System.getenv("host") + url;
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
        url = "http://" + System.getenv("host") + url;
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

    public static HttpResponse<JsonNode> uploadFile(String absPath, String gameId) {
        try {
            String url = "http://" + System.getenv("host") + "/api/game/" + gameId + "/music/upload";

            HttpResponse<JsonNode> response = Unirest.post(url)
                .header("Authorization", ClientApp.token)
                .field("file", new File(absPath))
                .asJson();

            System.out.println("Successfully uploaded music file");
            System.out.println(response.getBody().toString());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String downloadFile(String fileName, String gameId) {
        try {
            String url = "http://" + System.getenv("host") + "/api/game/" + gameId + "/music/download/" + fileName;

            var response = Unirest.get(url)
                .header("Authorization", ClientApp.token)
                .asFile(System.getProperty("user.dir") + (System.getProperty("user.dir").contains("assets") ? "" : "\\assets") + "\\downloads\\" + fileName);

            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
