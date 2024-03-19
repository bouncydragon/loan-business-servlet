package org.example.loanbusinessv3.repository;

import java.util.List;

import org.example.loanbusinessv3.model.Accounts;
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
    public void addLoan(List<Loans> loans, Accounts account) {
        EntityManager em = emf.createEntityManager();

        System.out.println(account);
        
        try {
            em.getTransaction().begin();
            for (Loans loan : loans) {
                Loans loanData = new Loans(loan.getLoan_amount(), loan.getInterest_rate(), loan.getStatus());
                loanData.setAccount_id(account);
                em.persist(loanData);
            };
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Loans> getAllLoans() {
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Loans> cq = cb.createQuery(Loans.class);
        Root<Loans> root = cq.from(Loans.class);
        CriteriaQuery<Loans> recs = cq.select(root);
        return em.createQuery(recs).getResultList();
    }

    @Override
    public Loans getLoanById(String loanId) {
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
    public void updateLoans(List<Loans> loansToUpdate) {
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
            System.out.println("Failed to update loans: " + e.getMessage());
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
