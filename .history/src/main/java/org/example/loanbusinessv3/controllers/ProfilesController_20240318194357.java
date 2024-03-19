package org.example.loanbusinessv3.controllers;

import java.io.IOException;
import java.util.*;

import org.example.loanbusinessv3.repository.ProfilesRepository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;

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
        getProfileAndAccount(req, resp);
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

    private void getProfileAndAccount(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String email = req.getParameter("email");
        Map<String, Object> retrievedProfile = profileRepo.selectProfile(email);

        String acctAndProfile = gson.toJson(retrievedProfile);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(acctAndProfile);
    }
}
