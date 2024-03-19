package org.example.loanbusinessv3.repository;

import java.util.List;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Profiles;
import org.example.loanbusinessv3.repository.dao.ProfilesDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

public class ProfilesRepository implements ProfilesDAO {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("LoanBusiness");

    @Override
    public void deleteProfile() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Profiles> getAllProfiles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void insertProfile(Profiles details) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Profiles selectProfile(String email) {
        EntityManager em = emf.createEntityManager();
    
        try {
            Accounts acct = em.fin




            // CriteriaBuilder cb = em.getCriteriaBuilder();
            // CriteriaQuery<Accounts> cq = cb.createQuery(Accounts.class);
            // Root<Accounts> root = cq.from(Accounts.class);
            // root.fetch("profile", JoinType.LEFT);
            // cq.select(root);
            // cq.where(cb.equal(root.get("email"), email));
            // return em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void updateProfile() {
        // TODO Auto-generated method stub
        
    }

    
    
}
