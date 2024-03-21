package org.example.loanbusinessv3.repository.dao;

import java.util.List;
import java.util.Map;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Loans;

public interface LoansDAO {
    void createLoan(List<Loans> loans, Accounts account);

    void updateLoan(List<Loans> loans);

    List<Loans> findAllLoans();

    Loans findLoanById(String loanId);

    void removeLoan(String loanId);

    Map<String, Object> createLoanWithGuarantors(List<Loans> loansWithGuarantors);

}
