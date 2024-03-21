package org.example.loanbusinessv3.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Profiles;
import org.example.loanbusinessv3.repository.AccountsRepository;
import org.example.loanbusinessv3.util.ResponseHandler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


@WebServlet(name="AccountsController", urlPatterns = {
    "/accounts", "/get-account", "/get-all-accounts",
})
public class AccountsController extends HttpServlet {

    private String email;
    private final AccountsRepository accountRepo = new AccountsRepository();
    Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BufferedReader reader =  req.getReader();

        JsonObject jsonObj = gson.fromJson(reader, JsonObject.class);
        String email = jsonObj.get("email").getAsString();

        String address = jsonObj.get("address").getAsString();
        String fullName = jsonObj.get("full_name").getAsString();
        String phone = jsonObj.get("phone").getAsString();

        try {
            Accounts newAccount = new Accounts(email);
            Accounts acctResponse = accountRepo.createAccount(newAccount);
    
            Profiles newProfile = new Profiles(fullName, phone, address);
            newProfile.setAccount(acctResponse);
    
            Profiles profResponse = accountRepo.createProfile(newProfile);
            ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, profResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        BufferedReader reader =  req.getReader();

        JsonObject jsonObj = gson.fromJson(reader, JsonObject.class);
        JsonObject accountObject = jsonObj.getAsJsonObject("account");
        JsonObject profileObject = jsonObj.getAsJsonObject("profiles");

        Long accountId = accountObject.get("account_id").getAsLong();
        Profiles profile = gson.fromJson(profileObject, Profiles.class);

        Accounts account = accountRepo.findById(accountId);

        if (account != null) {
            Profiles existingProfile = account.getProfile();
            if (existingProfile != null && existingProfile.getId() == profile.getId()) {

                existingProfile.setFull_name(profile.getFull_name());
                existingProfile.setPhone(profile.getPhone());
                existingProfile.setAddress(profile.getAddress());
                
                accountRepo.updateProfile(existingProfile);

                /*
                 * TODO PROBLEM: STATUS 500 Stackoverflow error
                 */
                ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, existingProfile);
            } else {
                ResponseHandler.jsonResponse(res, HttpServletResponse.SC_NOT_FOUND, "Profile not found");
            }
        } else {
            ResponseHandler.jsonResponse(res, HttpServletResponse.SC_NOT_FOUND, "Account not found");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        email = req.getParameter("email");
        try {
            accountRepo.removeAccount(email);
            ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, 
            "Successfully deleted email " + email + " and it's associated profile");
        } catch (Exception e) {
            ResponseHandler.jsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String actionPath = req.getServletPath();

        if (Objects.equals(actionPath, "/get-account")) {
            this.getAccount(req, resp);
        }

        if (Objects.equals(actionPath, "/get-all-accounts")) {
            this.getAllAccounts(resp);
        }
    }

    private void getAccount(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BufferedReader reader =  req.getReader();

        Accounts account = gson.fromJson(reader, Accounts.class);
        Map<String, Object> profile = accountRepo.findByEmailWithProfile(account.getEmail());

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, profile);
    }

    private void getAllAccounts(HttpServletResponse res) throws IOException {
        List<Accounts> accounts = accountRepo.findAllWithProfiles();

        List<Map<String, Object>> accountList = new ArrayList<>();

        for (Accounts a : accounts) {
            Map<String, Object> accountMap = new HashMap<>();
            accountMap.put("account_id", a.getAccount_id());
            accountMap.put("email", a.getEmail());
            accountMap.put("created_at", a.getCreated_at().toString());

            Map<String, Object> profileMap = new HashMap<>();

            if (a.getProfile() != null) {
                profileMap.put("profile_id", a.getProfile().getId());
                profileMap.put("full_name", a.getProfile().getFull_name());
                profileMap.put("phone", a.getProfile().getPhone());
                profileMap.put("address", a.getProfile().getAddress());

                accountMap.put("profiles", profileMap);
            } else {
                accountMap.put("profiles", new HashMap<>());
            }

            accountList.add(accountMap);
        }
        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, accountList);
    }
}
