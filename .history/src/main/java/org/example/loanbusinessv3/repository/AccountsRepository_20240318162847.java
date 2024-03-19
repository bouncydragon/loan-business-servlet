package org.example.loanbusinessv3.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.loanbusinessv3.repository.dao.AccountsDAO;
import org.example.loanbusinessv3.model.Accounts;

import java.util.List;

public class AccountsRepository implements AccountsDAO {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("LoanBusiness");

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
        Root<Accounts> rootEntry = cq.from(Accounts.class);
        CriteriaQuery<Accounts> all = cq.select(rootEntry);

        return em.createQuery(all).getResultList();
    }

    @Override
    public Accounts selectAccount(String email) {
        EntityManager em = emf.createEntityManager();
        // JPQL
         Accounts acct = em.createQuery("FROM Accounts acct WHERE acct.email = :email", Accounts.class)
                    .setParameter("email", email)
                    .getSingleResult();
         em.close();
         return acct;
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
        } catch (NoResultException e) {
            System.out.println("Email is not found!");
            e.printStackTrace();
            em.getTransaction().rollback();
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
