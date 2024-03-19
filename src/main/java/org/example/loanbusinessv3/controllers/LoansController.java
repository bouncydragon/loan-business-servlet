package org.example.loanbusinessv3.controllers;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

import com.google.gson.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Loans;
import org.example.loanbusinessv3.model.Status;
import org.example.loanbusinessv3.repository.AccountsRepository;
import org.example.loanbusinessv3.repository.LoansRepository;
import org.example.loanbusinessv3.util.LocalDateTypeAdapter;
import org.example.loanbusinessv3.util.ResponseHandler;


@WebServlet(name = "LoansController", urlPatterns = {
    "/loans", "/get-all-loans", "/get-loan-by-email",
    "/get-loan-by-id"
})
public class LoansController extends HttpServlet {

    private final AccountsRepository accountRepo = new AccountsRepository();
    private final LoansRepository loansRepo = new LoansRepository();

    private Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
                .create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        
        String email = jsonObject.get("email").getAsString();

        JsonArray loansArray = jsonObject.getAsJsonArray("loans");
        List<Loans> loans = new ArrayList<>();

        for (JsonElement element : loansArray) {
            Loans loan = gson.fromJson(element, Loans.class);
            loans.add(loan);
        }

        Accounts account = accountRepo.selectAccount(email);
        loansRepo.addLoan(loans, account);

        ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_OK, "Loans are successfully added to " + email);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

        List<Loans> loansToUpdate = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            JsonObject loanObject = element.getAsJsonObject();

            // get loan object from JSON.
            long loanId = loanObject.get("loan_id").getAsLong();
            double loanAmount = loanObject.get("loan_amount").getAsDouble();
            double interestRate = loanObject.get("interest_rate").getAsDouble();
            String status = loanObject.get("status").getAsString();

            Loans loan = new Loans();
            loan.setLoan_id(loanId);
            loan.setLoan_amount(loanAmount);
            loan.setInterest_rate(interestRate);
            loan.setStatus(Status.valueOf(status));

            loansToUpdate.add(loan);
        }

        loansRepo.updateLoans(loansToUpdate);

        ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_OK, "Loans updated successfully");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loanId = req.getParameter("loanId");
        loansRepo.removeLoan(loanId);

        ResponseHandler.jsonResponse(resp, HttpServletResponse.SC_OK, "Loan " + loanId + " is successfully removed");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String urlPath = req.getServletPath();

        if (Objects.equals(urlPath, "/get-all-loans")) {
            getAllLoans(req, resp);
        }
        if (Objects.equals(urlPath, "/get-loan-by-id")) {
            getLoanById(req, resp);
        }
    }

    private void getAllLoans(HttpServletRequest req, HttpServletResponse res) throws IOException {
        List<Loans> loans = loansRepo.getAllLoans();

        List<Map<String, Object>> allLoans = new ArrayList<>();

        for (Loans l : loans) {
            Map<String, Object> loanInfo = new HashMap<>();
            loanInfo.put("loan_id", l.getLoan_id());
            loanInfo.put("interest_rate", l.getInterest_rate());
            loanInfo.put("loan_amount", l.getLoan_amount());
            loanInfo.put("loan_id", l.getLoan_id());
            loanInfo.put("start_date", l.getStart_date());
            loanInfo.put("end_date", l.getEnd_date());
            loanInfo.put("status", l.getStatus());

            allLoans.add(loanInfo);
        }

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, allLoans);
    }

    private void getLoanById(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String loanId = req.getParameter("loanId");

        Loans loanData = loansRepo.getLoanById(loanId);

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, loanData);
    }

}
