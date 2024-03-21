// package org.example.loanbusinessv3.controllers;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;

// import org.example.loanbusinessv3.model.Guarantors;
// import org.example.loanbusinessv3.repository.GuarantorRepository;
// import org.example.loanbusinessv3.util.ResponseHandler;

// import com.google.gson.Gson;
// import com.google.gson.JsonArray;
// import com.google.gson.JsonElement;
// import com.google.gson.JsonObject;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.WebServlet;
// import jakarta.servlet.http.HttpServlet;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @WebServlet(name = "GuarantorsController", urlPatterns = {
//     "/guarantors"
// })
// public class GuarantorController extends HttpServlet {

//     private final GuarantorRepository guarantorRepo = new GuarantorRepository();
//     private Gson gson = new Gson();

//     @Override
//     protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//         BufferedReader reader = req.getReader();
//         JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

//         List<Guarantors> createGuarantors = new ArrayList<>();

//         for (JsonElement element : jsonArray) {
//             JsonObject guarantorObject = element.getAsJsonObject();

//             String guarantorName = guarantorObject.get("full_name").getAsString();
//             String guarantorEmail = guarantorObject.get("email").getAsString();
//             String guarantorPhone = guarantorObject.get("phone").getAsString();

//             Guarantors guarantor = new Guarantors(guarantorName, guarantorEmail, guarantorPhone);

//             createGuarantors.add(guarantor);
//         }
//         try {
//             guarantorRepo.createGuarantors(createGuarantors);
//             ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_OK, "Successfully added all guarantors.");
//         } catch (Exception e) {
//             ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
//         }
//     }

//     @Override
//     protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//         // TODO Auto-generated method stub
//         super.doPut(req, resp);
//     }

//     @Override
//     protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//         String email = req.getParameter("email");

//         Guarantors guarantor = guarantorRepo.findByEmail(email);
//         ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_OK, guarantor);
//     }

    

    

    
    
// }
