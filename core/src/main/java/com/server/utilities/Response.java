package com.server.utilities;

public class Response<T> {
    public static Response UN_AUTHORIZED = new Response(401, "Unauthorized", null);
    public static Response FORBIDDEN = new Response(403, "Forbidden", null);
    public static Response NOT_FOUND = new Response(404, "Not Found", null);
    public static Response INTERNAL_SERVER_ERROR = new Response(500, "Internal Server Error", null);
    public static Response BAD_REQUEST = new Response(400);
    public static Response<Object> OK = new Response<Object>(200);
    private int status;
    private String message;
    private T body;
    private long timestamp;

    public Response(int status, String message, T body) {
        this.status = status;
        this.message = message;
        this.body = body;
        this.timestamp = System.currentTimeMillis();
    }

    public Response(int status, String message) {
        this(status, message, null);
    }

    public Response(int status) {
        this(status, null);
    }

    public int getStatus() {
        return status;
    }

    public Response<T> setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getBody() {
        return body;
    }

    public Response<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
