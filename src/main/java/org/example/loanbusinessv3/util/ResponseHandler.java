package org.example.loanbusinessv3.util;

import java.io.IOException;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServletResponse;

public class ResponseHandler {

    private static class Message {
        @SuppressWarnings("unused")
        private String message;

        public Message(String message) {
            this.message = message;
        }
    }

    public static void jsonResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        Gson gson = new Gson();

        Message messageObject = new Message(message);
        String json = gson.toJson(messageObject);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write(json);
    }
    
    public static void jsonResponse(HttpServletResponse response, int statusCode, Object object) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
                .create();
        String json = gson.toJson(object);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write(json);
    }
}
