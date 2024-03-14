package org.example.loanbusinessv3.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.loanbusinessv3.dao.AccountsDAO;
import org.example.loanbusinessv3.model.Accounts;

import java.sql.SQLException;
import java.util.List;

public class AccountsRepository implements AccountsDAO {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("LoanBusiness");
    EntityManager em = emf.createEntityManager();

    @Override
    public void insertAccount(Accounts details) {
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
        // Criteria API
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Accounts> cq = cb.createQuery(Accounts.class);
        Root<Accounts> rootEntry = cq.from(Accounts.class);
        CriteriaQuery<Accounts> all = cq.select(rootEntry);

        return em.createQuery(all).getResultList();
    }

    @Override
    public Accounts selectAccount(String email) {
        // JPQL
        return em.createQuery("FROM Accounts acct WHERE acct.email = :email", Accounts.class)
                    .setParameter("email", email)
                    .getSingleResult();
    }

    @Override
    public void updateAccount(String email) throws SQLException {

    }

    @Override
    public void deleteAccount(String email) throws SQLException {

    }
}
