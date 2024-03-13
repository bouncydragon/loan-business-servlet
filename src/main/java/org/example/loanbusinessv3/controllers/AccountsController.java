package org.example.loanbusinessv3.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.repository.AccountsRepository;


@WebServlet(name="AccountsController", value = "/accounts-add")
public class AccountsController extends HttpServlet {

    private AccountsRepository accountRepo;

    @Override
    public void init() {
        accountRepo = new AccountsRepository();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        Accounts newAcct = new Accounts(email);
        accountRepo.insertAccount(newAcct);
        resp.sendRedirect("List");
    }
}
