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
import org.example.loanbusinessv3.model.Guarantors;
import org.example.loanbusinessv3.model.Loans;
import org.example.loanbusinessv3.model.Profiles;
import org.example.loanbusinessv3.model.Status;
import org.example.loanbusinessv3.repository.AccountsRepository;
// import org.example.loanbusinessv3.repository.GuarantorRepository;
import org.example.loanbusinessv3.repository.LoansRepository;
import org.example.loanbusinessv3.util.LocalDateTypeAdapter;
import org.example.loanbusinessv3.util.ResponseHandler;


@WebServlet(name = "LoansController", urlPatterns = {
    "/loans", "/get-all-loans", "/get-loan-by-email",
    "/get-loan-by-id", "/loans-guarantors"
})
public class LoansController extends HttpServlet {

    private final AccountsRepository accountRepo = new AccountsRepository();
    private final LoansRepository loansRepo = new LoansRepository();
    // private final GuarantorRepository guarantorRepo = new GuarantorRepository();

    private Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
                .create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String urlPath = req.getServletPath();

        // if (Objects.equals(urlPath, "/loans-guarantors")) {
        //     assocLoansAndGuarantors(req, resp);
        // } else {
            createLoansWithAssocAccount(req, resp);
        // }
    }

    // private void assocLoansAndGuarantors(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    //     BufferedReader reader = req.getReader();
    //     JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

    //     JsonArray loansArray = jsonObject.getAsJsonArray("loans");
    //     JsonArray guarantorsList = jsonObject.getAsJsonArray("guarantors");
    //     List<Loans> loansWithGuarantors = new ArrayList<>();

    //     for (JsonElement lElement : loansArray) {
    //         Loans loan = gson.fromJson(lElement, Loans.class);
    //         List<Guarantors> loanGuarantors = new ArrayList<>();

    //         for (JsonElement gElement : guarantorsList) {
    //             String email = gElement.getAsString();
    //             Guarantors guarantor = guarantorRepo.findByEmail(email);
    //             if (guarantor != null) {
    //                 loanGuarantors.add(guarantor);
    //             }
    //         }
    //         loan.setGuarantors(loanGuarantors);
    //         loansWithGuarantors.add(loan);
    //     }
    //     Map<String, Object> list = loansRepo.createLoanWithGuarantors(loansWithGuarantors);
    //     ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, list);
    // }

    private void createLoansWithAssocAccount(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        
        Long accountId = jsonObject.get("account_id").getAsLong();

        JsonArray loansArray = jsonObject.getAsJsonArray("loans");
        List<Loans> loans = new ArrayList<>();

        for (JsonElement element : loansArray) {
            Loans loan = gson.fromJson(element, Loans.class);
            loans.add(loan);
        }

        Accounts account = accountRepo.findById(accountId);
        loansRepo.createLoan(loans, account);

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, "Loans are successfully added to " + account.getEmail());
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

        loansRepo.updateLoan(loansToUpdate);

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
            findAllLoans(req, resp);
        }
        if (Objects.equals(urlPath, "/get-loan-by-id")) {
            findLoanById(req, resp);
        }
    }

    private void findAllLoans(HttpServletRequest req, HttpServletResponse res) throws IOException {
        List<Loans> loans = loansRepo.findAllLoans();

        List<Map<String, Object>> allLoans = new ArrayList<>();

        for (Loans l : loans) {
            Map<String, Object> loanInfo = new HashMap<>();
            loanInfo.put("account", l.getAccount_id());
            loanInfo.put("loan_id", l.getLoan_id());
            loanInfo.put("loan_amount", l.getLoan_amount());
            loanInfo.put("interest_rate", l.getInterest_rate());
            loanInfo.put("start_date", l.getStart_date());
            loanInfo.put("end_date", l.getEnd_date());
            loanInfo.put("status", l.getStatus());

            // need to add the guarantors

            allLoans.add(loanInfo);
        }

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, allLoans);
    }

    private void findLoanById(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String loanId = req.getParameter("loanId");

        System.out.println(loanId);

        Loans loanData = loansRepo.findLoanById(loanId);

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, loanData);
    }

}
