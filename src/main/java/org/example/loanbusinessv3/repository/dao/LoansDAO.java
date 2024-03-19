package org.example.loanbusinessv3.repository.dao;

import java.util.List;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Loans;

public interface LoansDAO {
    void addLoan(List<Loans> loans, Accounts account);

    void removeLoan(String loanId);

    void updateLoans(List<Loans> loans);

    List<Loans> getAllLoans();

    Loans getLoanById(String loanId);
}
