package org.example.loanbusinessv3.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.example.loanbusinessv3.model.Guarantors;
import org.example.loanbusinessv3.repository.GuarantorRepository;
import org.example.loanbusinessv3.util.ResponseHandler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "GuarantorsController", urlPatterns = {
    "/guarantors", "/find-all-guarantors", "/find-guarantor"
})
public class GuarantorController extends HttpServlet {

    private final GuarantorRepository guarantorRepo = new GuarantorRepository();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

        List<Guarantors> createGuarantors = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            JsonObject guarantorObject = element.getAsJsonObject();

            String guarantorName = guarantorObject.get("full_name").getAsString();
            String guarantorEmail = guarantorObject.get("email").getAsString();
            String guarantorPhone = guarantorObject.get("phone").getAsString();

            Guarantors guarantor = new Guarantors(guarantorName, guarantorEmail, guarantorPhone);

            createGuarantors.add(guarantor);
        }
        try {
            guarantorRepo.createGuarantors(createGuarantors);
            ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_OK, "Successfully added all guarantors.");
        } catch (Exception e) {
            ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

        List<Guarantors> guarantorsToUpdate = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            JsonObject guarantorObj = element.getAsJsonObject();

            // get guarantor object from JSON.
            long id = guarantorObj.get("guarantor_id").getAsLong();
            String name = guarantorObj.get("full_name").getAsString();
            String email = guarantorObj.get("email").getAsString();
            String phone = guarantorObj.get("phone").getAsString();

            Guarantors guarantor = new Guarantors();
            guarantor.setGuarantor_id(id);
            guarantor.setFull_name(name);
            guarantor.setEmail(email);
            guarantor.setPhone(phone);

            guarantorsToUpdate.add(guarantor);
        }

        try {
            guarantorRepo.updateGuarantors(guarantorsToUpdate);
            ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_OK, "Guarantors updated successfully.");
        } catch (Exception e) {
            ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionPath = req.getServletPath();

        if (Objects.equals(actionPath, "/find-guarantor")) {
            this.findbyEmail(req, resp);
        }

        if (Objects.equals(actionPath, "/find-all-guarantors")) {
            this.findAllGuarantors(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("guarantorId");

        try {
            guarantorRepo.removeGuarantor(id);
        ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_OK, "Successfully removed guarantor.");
        } catch (Exception e) {
        ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void findbyEmail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        Guarantors guarantor = guarantorRepo.findByEmail(email);
        ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_OK, guarantor);
    }

    private void findAllGuarantors(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Guarantors> guarantors = guarantorRepo.findAllGuarantors();

        List<Map<String, Object>> allGuarantors = new ArrayList<>();

        for (Guarantors g : guarantors) {
            Map<String, Object> guarantorInfo = new HashMap<>();
            guarantorInfo.put("guarantor_id", g.getGuarantor_id());
            guarantorInfo.put("full_name", g.getFull_name());
            guarantorInfo.put("phone", g.getPhone());
            guarantorInfo.put("email", g.getEmail());

            allGuarantors.add(guarantorInfo);
        }

        ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_OK, allGuarantors);
    }
}
