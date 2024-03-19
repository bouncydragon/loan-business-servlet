package org.example.loanbusinessv3.controllers;

import org.example.loanbusinessv3.util.LocalDateTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "ProfilesController", urlPatterns = {
    "/profiles"
})
public class ProfilesController {

    private String fullName;

    private Gson gson = new GsonBuilder().create();
}
