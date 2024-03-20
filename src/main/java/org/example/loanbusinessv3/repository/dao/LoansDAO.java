package org.example.loanbusinessv3.repository.dao;

import java.util.List;
import java.util.Map;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Loans;

public interface LoansDAO {
    void addLoan(List<Loans> loans, Accounts account);

    Map<String, Object> createLoanWithGuarantors(List<Loans> loansWithGuarantors);

    void removeLoan(String loanId);

    void updateLoans(List<Loans> loans);

    List<Loans> getAllLoans();

    Loans getLoanById(String loanId);
}
