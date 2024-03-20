package org.example.loanbusinessv3.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.loanbusinessv3.repository.dao.AccountsDAO;
import org.example.loanbusinessv3.util.EntityManagerUtil;
import org.example.loanbusinessv3.model.Accounts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountsRepository implements AccountsDAO {
    
    @PersistenceUnit
    private EntityManagerFactory emf = EntityManagerUtil.createEntityManagerFactory();

    @Override
    public void insertAccount(Accounts details) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(details);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Accounts> selectAllAccounts() {
        EntityManager em = emf.createEntityManager();
        // Criteria API
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Accounts> cq = cb.createQuery(Accounts.class);
        Root<Accounts> root = cq.from(Accounts.class);
        CriteriaQuery<Accounts> recs = cq.select(root);

        return em.createQuery(recs).getResultList();
    }

    @Override
    public Accounts selectAccount(String email) {
        EntityManager em = emf.createEntityManager();
        // JPQL
        try {
            Accounts resultAccount = em.createQuery("FROM Accounts acct WHERE acct.email = :email", Accounts.class)
            .setParameter("email", email)
            .getSingleResult();

            Accounts account = new Accounts();
            account.setAccount_id(resultAccount.getAccount_id());
            account.setCreated_at(resultAccount.getCreated_at());
            account.setEmail(resultAccount.getEmail());

            return account;
        } finally {
            em.close();
        }
    }

    @Override
    public void updateAccount(String email, String updatedEmail) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("UPDATE Accounts acct SET acct.email = ?1 WHERE acct.email = ?2")
                    .setParameter(1, updatedEmail)
                    .setParameter(2, email)
                    .executeUpdate();
                    
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteAccount(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Accounts acct WHERE acct.email = ?1")
                    .setParameter(1, email)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
