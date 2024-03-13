package org.example.loanbusinessv3.repository;

import jakarta.persistence.*;
import org.example.loanbusinessv3.dao.AccountsDAO;
import org.example.loanbusinessv3.model.Accounts;

import java.sql.SQLException;
import java.util.List;

public class AccountsRepository implements AccountsDAO {

    @Override
    public void insertAccount(Accounts details) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("LoanBusiness");
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
        return null;
    }

    @Override
    public Accounts selectAccount(String email) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("LoanBusiness");
        EntityManager em = emf.createEntityManager();

        Accounts acctByEmail = null;
        try {
            em.getTransaction().begin();
            acctByEmail = em.createQuery("FROM Accounts acct WHERE acct.email = :email", Accounts.class)
                    .setParameter("email", email)
                    .getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return acctByEmail;
    }

    @Override
    public void updateAccount(String email) throws SQLException {

    }

    @Override
    public void deleteAccount(String email) throws SQLException {

    }
}
