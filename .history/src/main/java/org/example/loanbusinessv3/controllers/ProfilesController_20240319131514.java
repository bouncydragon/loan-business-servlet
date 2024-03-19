package org.example.loanbusinessv3.controllers;

import java.io.IOException;
import java.util.*;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Profiles;
import org.example.loanbusinessv3.repository.AccountsRepository;
import org.example.loanbusinessv3.repository.ProfilesRepository;
import org.example.loanbusinessv3.util.HandleError;
import org.example.loanbusinessv3.util.LocalDateTypeAdapter;

@WebServlet(name = "ProfilesController", urlPatterns = {
    "/profiles", "/get-account-profile", "/get-profile",
    "/get-all-profile", "/delete-profile"
})
public class ProfilesController extends HttpServlet {

    private final ProfilesRepository profileRepo = new ProfilesRepository();
    private final AccountsRepository accountRepo = new AccountsRepository();
    
    private Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .create();

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        profileRepo.deleteProfile(email);

        resp.setStatus(200);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String urlPath = req.getServletPath();

        if (Objects.equals(urlPath, "/get-account-profile")) {
            getProfileAndAccount(req, resp);
        }
        if (Objects.equals(urlPath, "/get-profile")) {
            getProfile(req, resp);
        }
        if (Objects.equals(urlPath, "/get-all-profile")) {
            getAllProfiles(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String email = req.getParameter("email");

        String errorHandle;

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Profiles existingProfile = profileRepo.selectProfile(email);

        if (existingProfile != null) {
            HandleError error = new HandleError(
                "Email " + email + " is already associated with a profile.", 
                HttpServletResponse.SC_CONFLICT,
                "Create a new account and profile.");
            errorHandle = gson.toJson(error);
            out.print(errorHandle);
            return;
        }

        Profiles newProfile = new Profiles(fullName, phone, address);
        Accounts account = accountRepo.selectAccount(email);

        if (account != null) {
            account.setProfile(newProfile);
            newProfile.setAccount_id(account);
    
            profileRepo.insertProfile(newProfile);
            resp.getWriter().println("Profile successfully created!");
        } else {
            HandleError error = new HandleError(
                "Email " + email + " does not exist!", 
                HttpServletResponse.SC_CONFLICT,
                "Create a new account.");
            errorHandle = gson.toJson(error);
            out.print(errorHandle);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doPut(req, resp);
    }

    private void getProfileAndAccount(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String email = req.getParameter("email");
        Map<String, Object> retrievedProfile = profileRepo.selectProfileAndAccount(email);

        String acctAndProfile = gson.toJson(retrievedProfile);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(acctAndProfile);
    }

    private void getProfile(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String email = req.getParameter("email");
        Profiles retrievedProfile = profileRepo.selectProfile(email);

        String profileDets = gson.toJson(retrievedProfile);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(profileDets);
    }

    private void getAllProfiles(HttpServletRequest req, HttpServletResponse res) throws IOException {
        List<Profiles> profiles = profileRepo.getAllProfiles();

        res.setStatus(200);
        String allProfiles = gson.toJson(profiles);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(allProfiles);
    }
    
}