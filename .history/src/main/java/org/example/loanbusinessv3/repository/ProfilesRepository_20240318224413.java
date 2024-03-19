package org.example.loanbusinessv3.repository;

import java.util.*;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Profiles;
import org.example.loanbusinessv3.repository.dao.ProfilesDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class ProfilesRepository implements ProfilesDAO {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Override
    public void deleteProfile() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Profiles> getAllProfiles() {
        EntityManager em = emf.createEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Profiles> cq = cb.createQuery(Profiles.class);
        Root<Profiles> root = cq.from(Profiles.class);
        CriteriaQuery<Profiles> recs = cq.select(root);

        return em.createQuery(recs).getResultList();
    }

    @Override
    public void insertProfile(Profiles details) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Map<String, Object> selectProfileAndAccount(String email) {
        EntityManager em = emf.createEntityManager();
    
        Accounts account = em.createNamedQuery("findAccountByEmail", Accounts.class)
            .setParameter("email", email)
            .getSingleResult();

        if (account != null) {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> accountDetails = new HashMap<>();
            
            accountDetails.put("account_id", account.getAccount_id());
            accountDetails.put("email", account.getEmail());

            Profiles profile = account.getProfile();
            Map<String, Object> profileDetails = new HashMap<>();
            profileDetails.put("fullName", profile.getFull_name());
            profileDetails.put("phone", profile.getPhone());
            profileDetails.put("address", profile.getAddress());

            response.put("account", accountDetails);
            response.put("profile", profileDetails);

            em.close();
            return response;
        }
        return null;
    }

    @Override
    public Profiles selectProfile(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            Accounts account = em.createNamedQuery("findAccountByEmail", Accounts.class)
            .setParameter("email", email)
            .getSingleResult();
            
            return account.getProfile();
        } finally {
            em.close();
        }
    }

    @Override
    public void updateProfile() {
        // TODO Auto-generated method stub
        
    }
}
