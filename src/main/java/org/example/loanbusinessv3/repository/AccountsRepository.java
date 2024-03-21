package org.example.loanbusinessv3.repository;

import jakarta.persistence.*;
import org.example.loanbusinessv3.repository.dao.AccountsDAO;
import org.example.loanbusinessv3.repository.dao.ProfilesDAO;
import org.example.loanbusinessv3.util.EntityManagerUtil;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Profiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountsRepository implements AccountsDAO, ProfilesDAO {
    
    @PersistenceUnit
    private EntityManagerFactory emf = EntityManagerUtil.createEntityManagerFactory();

    @Override
    public Accounts createAccount(Accounts account) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(account);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
        return account;
    }

    @Override
    public Profiles createProfile(Profiles profile) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(profile);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
        return profile;
    }

    @Override
    public Map<String, Object> findByEmailWithProfile(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Accounts> query = em.createQuery(
            "SELECT a FROM Accounts a JOIN FETCH a.profile WHERE a.email = :email", Accounts.class);
            query.setParameter("email", email);

            Accounts account = query.getSingleResult();

            if (account != null) {
                Profiles profile = account.getProfile();

                Map<String, Object> response = new HashMap<>();
                Map<String, Object> accountDetails = new HashMap<>();
                
                accountDetails.put("account_id", account.getAccount_id());
                accountDetails.put("email", account.getEmail());
                accountDetails.put("created_at", account.getCreated_at());

                Map<String, Object> profileDetails = new HashMap<>();
                profileDetails.put("profile_id", profile.getId());
                profileDetails.put("address", profile.getAddress());
                profileDetails.put("fullName", profile.getFull_name());
                profileDetails.put("phone", profile.getPhone());

                response.put("account", accountDetails);
                response.put("profile", profileDetails);

                em.close();
                return response;
            }
            throw new NoResultException("Account " + email + " does not exist.");
        } catch (NoResultException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            em.close();
        }
    }

    @Override
    public List<Accounts> findAllWithProfiles() {
        EntityManager em = emf.createEntityManager();

        return em.createQuery("SELECT a FROM Accounts a LEFT JOIN FETCH a.profile", Accounts.class).getResultList();
    }
    
    @Override
    public Accounts findById(Long accountId) {
        EntityManager em = emf.createEntityManager();
        return em.find(Accounts.class, accountId);
    }

    @Override
    public Profiles updateProfile(Profiles profile) {
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();
            em.merge(profile);
            em.getTransaction().commit();
            return profile;
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
    
    @Override
    public void removeAccount(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            TypedQuery<Accounts> query = em.createNamedQuery("findAccountByEmail", Accounts.class);
            query.setParameter("email", email);
            Accounts account = query.getSingleResult();
            if (account == null) {
                throw new EntityNotFoundException("Account not found with email: " + email);
            }

            Profiles profile = account.getProfile();
            if (profile != null) {
                em.remove(profile);
            }

            em.remove(account);

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
