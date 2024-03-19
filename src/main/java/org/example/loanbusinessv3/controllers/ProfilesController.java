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

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Profiles existingProfile = profileRepo.selectProfile(email);

        if (existingProfile != null) {
            HandleError error = new HandleError(
                "Email " + email + " is already associated with a profile.", 
                HttpServletResponse.SC_CONFLICT,
                "Create a new account and profile.");
            String errorHandle = gson.toJson(error);
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
            String errorHandle = gson.toJson(error);
            out.print(errorHandle);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String email = req.getParameter("email");

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Profiles existingProfile = profileRepo.selectProfile(email);

        if (existingProfile == null) {
            HandleError error = new HandleError(
                "Email " + email + " does not exist.", 
                HttpServletResponse.SC_CONFLICT,
                "Create a new account.");
            String errorHandle = gson.toJson(error);
            out.print(errorHandle);
            return;
        }

        Profiles profile = profileRepo.selectProfile(email);
        if (profile != null) {
            Map<String, String> params = new HashMap<>();
            params.put("fullName", fullName);
            params.put("address", address);
            params.put("phone", phone);

            profileRepo.updateProfile(params, email);
        }
    }

    /* 
     * Calling this methods everything works fine. Below is the output of the getProfileAndAccount method
     * Desired Output:
     * {
            "profile": {
                "address": "NY. GC.",
                "phone": "09778937463",
                "fullName": "Flonta"
            },
            "account": {
                "account_id": 15,
                "email": "dev.test-07@gmail.com"
            }
        }
     * */ 
    private void getProfileAndAccount(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String email = req.getParameter("email");
        Map<String, Object> retrievedProfile = profileRepo.selectProfileAndAccount(email);

        String acctAndProfile = gson.toJson(retrievedProfile);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(acctAndProfile);
    }

    /*
     * Problem : Returns Stackoverflow error
     * Solution : selectProfile() method in ProfilesRepository
     * Desired Output:
        {
           "address": "NY. GC.",
           "phone": "09778937463",
           "fullName": "Flonta"
        }
     * */ 
    private void getProfile(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String email = req.getParameter("email");
        Profiles retrievedProfile = profileRepo.selectProfile(email);

        String profileDets = gson.toJson(retrievedProfile);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(profileDets);
    }

    /* 
     * Problem : Returns Stackoverflow error
     * Desired Output:
        {
           {
                "address": "NY. GC.",
                "phone": "09778937463",
                "fullName": "Flonta"
            },
            {
                "address": "NY. GC.",
                "phone": "09778937463",
                "fullName": "Magiska"
            },
        }
     * */ 
    private void getAllProfiles(HttpServletRequest req, HttpServletResponse res) throws IOException {
        List<Profiles> profiles = profileRepo.getAllProfiles();

         /*
         * Solution
         */
        List<Map<String, Object>> allProfilesDetails = new ArrayList<>();

        for (Profiles p : profiles) {
            Map<String, Object> profileDetails = new HashMap<>();
            profileDetails.put("address", p.getAddress());
            profileDetails.put("fullName", p.getFull_name());
            profileDetails.put("phone", p.getPhone());
            profileDetails.put("profile_id", p.getId());

            allProfilesDetails.add(profileDetails);
        }

        res.setStatus(200);
        String allProfiles = gson.toJson(allProfilesDetails);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(allProfiles);
    }
    
}
