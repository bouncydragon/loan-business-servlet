package org.example.loanbusinessv3.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Guarantors;
import org.example.loanbusinessv3.model.Loans;
import org.example.loanbusinessv3.repository.dao.LoansDAO;
import org.example.loanbusinessv3.util.EntityManagerUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class LoansRepository implements LoansDAO {

    @PersistenceUnit
    private EntityManagerFactory emf = EntityManagerUtil.createEntityManagerFactory();

    @Override
    public void createLoan(List<Loans> loans, Accounts account) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            for (Loans loan : loans) {
                Loans loanData = new Loans(loan.getLoan_amount(), loan.getInterest_rate(), loan.getStatus());
                loanData.setAccount_id(account);
                em.persist(loanData);
            };
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println(e);
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Map<String, Object> createLoanWithGuarantors(List<Loans> loansWithGuarantors) {
        EntityManager em = emf.createEntityManager();
        Map<String, Object> response = new HashMap<>();

        try {
            em.getTransaction().begin();
            List<Map<String, Object>> loansResponse = new ArrayList<>();
            for (Loans loan : loansWithGuarantors) {
                List<Map<String, Object>> guarantorsResponse = new ArrayList<>();
                for (Guarantors guarantor : loan.getGuarantors()) {
                    em.createNativeQuery("INSERT INTO loan_guarantors (loan_id, guarantor_id) VALUES (?, ?)")
                        .setParameter(1, loan.getLoan_id())
                        .setParameter(2, guarantor.getGuarantor_id())
                        .executeUpdate();

                    Map<String, Object> guarantorMap = new HashMap<>();
                    guarantorMap.put("id", guarantor.getGuarantor_id());
                    guarantorMap.put("fullName", guarantor.getFull_name());
                    guarantorMap.put("phone", guarantor.getPhone());
                    guarantorMap.put("email", guarantor.getEmail());
                    guarantorsResponse.add(guarantorMap);
                }
                Map<String, Object> loanMap = new HashMap<>();
                loanMap.put("loan_id", loan.getLoan_id());
                loanMap.put("interest_rate", loan.getInterest_rate());
                loanMap.put("loan_amount", loan.getLoan_amount());
                loanMap.put("start_date", loan.getStart_date());
                loanMap.put("end_date", loan.getEnd_date());
                loanMap.put("status", loan.getStatus());
                loanMap.put("guarantors", guarantorsResponse);
                loansResponse.add(loanMap);
            }
            response.put("loans", loansResponse);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return response;
    }

    @Override
    public List<Loans> findAllLoans() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                "SELECT DISTINCT l FROM Loans l " +
                "LEFT JOIN FETCH l.guarantors " +
                "LEFT JOIN FETCH l.account_id", Loans.class)
                .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Loans findLoanById(String loanId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Loans.class, loanId);
        } finally {
            em.close();
        }
    }

    @Override
    public void removeLoan(String loanId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Loans loan = em.find(Loans.class, loanId);
            if (loan != null) {
                em.remove(loan);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Loan with ID " + loanId + " not found.");
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void updateLoan(List<Loans> loansToUpdate) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
    
            for (Loans loan : loansToUpdate) {
                Loans existingLoan = em.find(Loans.class, loan.getLoan_id());
                if (existingLoan == null) {
                    System.out.println("Loan with ID " + loan.getLoan_id() + " does not exist.");
                    em.getTransaction().rollback();
                    return;
                }
    
                existingLoan.setLoan_amount(loan.getLoan_amount());
                existingLoan.setInterest_rate(loan.getInterest_rate());
                existingLoan.setStatus(loan.getStatus());
    
                em.merge(existingLoan);
            }
    
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }
}
