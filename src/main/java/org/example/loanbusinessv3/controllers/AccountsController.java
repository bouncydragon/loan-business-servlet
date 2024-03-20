package org.example.loanbusinessv3.controllers;

import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
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


@WebServlet(name="AccountsController", urlPatterns = {
    "/accounts", "/get-account", "/get-all-accounts",
})
public class AccountsController extends HttpServlet {

    private String email;
    private final AccountsRepository accountRepo = new AccountsRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        email = req.getParameter("email");

        Accounts newAcct = new Accounts(email);
        accountRepo.insertAccount(newAcct);

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, newAcct);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        email = req.getParameter("email");
        String updatedEmail = req.getParameter("updatedEmail");

        if (email == null || updatedEmail == null || email.isEmpty() || updatedEmail.isEmpty()) {
            ResponseHandler.jsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, "Please provide the proper parameters.");
        }

        if (!email.isEmpty() && !updatedEmail.isEmpty()) {
            accountRepo.updateAccount(email, updatedEmail);
            Accounts updatedAcct = null;

            try {
                updatedAcct = accountRepo.selectAccount(updatedEmail);
                ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, updatedAcct);
            } catch (NoResultException e) {
                ResponseHandler.jsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        email = req.getParameter("email");
        accountRepo.deleteAccount(email);

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, "Successfully deleted email " + email);
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
        email = req.getParameter("email");
        Accounts account = accountRepo.selectAccount(email);

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, account);
    }

    private void getAllAccounts(HttpServletResponse res) throws IOException {
        List<Accounts> accounts = accountRepo.selectAllAccounts();

        List<Map<String, Object>> accountList = new ArrayList<>();

        for (Accounts a : accounts) {
            Map<String, Object> accountObj = new HashMap<>();
            accountObj.put("account_id", a.getAccount_id());
            accountObj.put("email", a.getEmail());
            accountObj.put("created_at", a.getCreated_at());

            accountList.add(accountObj);
        }

        ResponseHandler.jsonResponse(res, HttpServletResponse.SC_OK, accountList);
    }
}
