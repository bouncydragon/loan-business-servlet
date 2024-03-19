package org.example.loanbusinessv3.controllers;

import java.io.IOException;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Profiles;
import org.example.loanbusinessv3.repository.AccountsRepository;
import org.example.loanbusinessv3.repository.ProfilesRepository;
import org.example.loanbusinessv3.util.ResponseHandler;

@WebServlet(name = "ProfilesController", urlPatterns = {
    "/profiles", "/get-account-profile", "/get-profile",
    "/get-all-profiles"
})
public class ProfilesController extends HttpServlet {

    private final ProfilesRepository profileRepo = new ProfilesRepository();
    private final AccountsRepository accountRepo = new AccountsRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String email = req.getParameter("email");

        Profiles existingProfile = profileRepo.selectProfile(email);

        if (existingProfile != null) {
            ResponseHandler.jsonResponse(res, HttpServletResponse.SC_CONFLICT, "Email " + email + " is already associated with a profile.");
            return;
        } else {
            Profiles newProfile = new Profiles(fullName, phone, address);
            Accounts account = accountRepo.selectAccount(email);
            if (account != null) {
                account.setProfile(newProfile);
                newProfile.setAccount_id(account);
        
                profileRepo.insertProfile(newProfile);
                /*
                 * Need to fix this response, it should return the object of Profile
                 * Getting an error of Stackoverflow
                 */
                ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, "Profile successfully created!");
            } else {
                ResponseHandler.jsonResponse(res, HttpServletResponse.SC_CONFLICT, "Email " + email + " does not exist!");
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String email = req.getParameter("email");

        Profiles existingProfile = profileRepo.selectProfile(email);

        if (existingProfile == null) {
            ResponseHandler.jsonResponse(res, HttpServletResponse.SC_CONFLICT, "Email " + email + " does not exist!");
            return;
        }

        Profiles profile = profileRepo.selectProfile(email);
        if (profile != null) {
            Map<String, String> params = new HashMap<>();
            params.put("fullName", fullName);
            params.put("address", address);
            params.put("phone", phone);

            profileRepo.updateProfile(params, email);
            /*
             * Should also return the updated profile ???
             */
            ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, "Profile of" + email + " successfully updated.");
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email");
        profileRepo.deleteProfile(email);

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, "Successfully deleted " + email);
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

    private void getProfileAndAccount(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String email = req.getParameter("email");
        Map<String, Object> retrievedProfile = profileRepo.selectProfileAndAccount(email);

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, retrievedProfile);
    }

    private void getProfile(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String email = req.getParameter("email");
        Profiles retrievedProfile = profileRepo.selectProfile(email);

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, retrievedProfile);
    }

    private void getAllProfiles(HttpServletRequest req, HttpServletResponse res) throws IOException {
        List<Profiles> profiles = profileRepo.getAllProfiles();

        List<Map<String, Object>> allProfilesDetails = new ArrayList<>();

        for (Profiles p : profiles) {
            Map<String, Object> profileDetails = new HashMap<>();
            profileDetails.put("address", p.getAddress());
            profileDetails.put("fullName", p.getFull_name());
            profileDetails.put("phone", p.getPhone());
            profileDetails.put("profile_id", p.getId());

            allProfilesDetails.add(profileDetails);
        }

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, allProfilesDetails);
    }
    
}
