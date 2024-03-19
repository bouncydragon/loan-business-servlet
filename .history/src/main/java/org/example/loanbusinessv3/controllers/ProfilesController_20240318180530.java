package org.example.loanbusinessv3.controllers;

import java.io.IOException;

import org.example.loanbusinessv3.model.Profiles;
import org.example.loanbusinessv3.repository.ProfilesRepository;
import org.example.loanbusinessv3.util.LocalDateTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ProfilesController", urlPatterns = {
    "/profiles"
})
public class ProfilesController extends HttpServlet {

    private String fullName;
    private ProfilesRepository profileRepo;
    private Gson gson = new GsonBuilder().create();

    @Override
    public void init() {
        profileRepo = new ProfilesRepository();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doDelete(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doPut(req, resp);
    }

    private void getProfileAndAccount(HttpServletRequest req, HttpServletResponse res) {
        String email = req.getParameter("email");
        Profiles retrievedProfile = profileRepo.selectProfile(email);
    }
}
