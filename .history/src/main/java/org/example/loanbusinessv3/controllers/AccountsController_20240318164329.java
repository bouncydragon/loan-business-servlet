package org.example.loanbusinessv3.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.repository.AccountsRepository;
import org.example.loanbusinessv3.util.LocalDateTypeAdapter;
import org.example.loanbusinessv3.util.HandleError;


@WebServlet(name="AccountsController", urlPatterns = {
        "/accounts", "/get-account",
        "/get-all-accounts", "/delete-account",
        "/update-account"
})
public class AccountsController extends HttpServlet {

    private String email;
    private AccountsRepository accountRepo;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();

    @Override
    public void init() {
        accountRepo = new AccountsRepository();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        email = req.getParameter("email");

        Accounts newAcct = new Accounts(email);
        accountRepo.insertAccount(newAcct);

        resp.setStatus(200);
        String newAcctJsonString = gson.toJson(newAcct);
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(newAcctJsonString);
        out.flush(); // Still don't understand what is this
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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        email = req.getParameter("email");
        accountRepo.deleteAccount(email);

        res.setStatus(200);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        email = req.getParameter("email");
        String updatedEmail = req.getParameter("updatedEmail");

        if (email == null || updatedEmail == null || email.isEmpty() || updatedEmail.isEmpty()) {
            HandleError error = new HandleError(
                    "Missing additional parameter",
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please provide the proper parameters."
            );

            String errorHandle = gson.toJson(error);
            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.print(errorHandle);
        }

        if (!email.isEmpty() && !updatedEmail.isEmpty()) {
            accountRepo.updateAccount(email, updatedEmail);
            Accounts updatedAcct = null;

            try {
                updatedAcct = accountRepo.selectAccount(updatedEmail);
            } catch (NoResultException e) {
System.out.println("pumasok?");
            }

            res.setStatus(200);

            String newAcctJsonString = gson.toJson(updatedAcct);
            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.print(newAcctJsonString);
        }
    }

    private void getAccount(HttpServletRequest req, HttpServletResponse res) throws IOException {
        email = req.getParameter("email");
        Accounts retrievedAcct = accountRepo.selectAccount(email);

        res.setStatus(200);
        String newAcctJsonString = gson.toJson(retrievedAcct);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(newAcctJsonString);
        out.flush();
    }

    private void getAllAccounts(HttpServletResponse res) throws IOException {
        List<Accounts> allAccounts = accountRepo.selectAllAccounts();

        res.setStatus(200);
        String newAcctJsonString = gson.toJson(allAccounts);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(newAcctJsonString);

    }
}
