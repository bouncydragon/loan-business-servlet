package org.example.loanbusinessv3.repository;

import java.util.List;

import org.example.loanbusinessv3.model.Accounts;
import org.example.loanbusinessv3.model.Profiles;
import org.example.loanbusinessv3.repository.dao.ProfilesDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

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
    
        TypedQuery<Accounts> query = em.createNamedQuery("findAccountByEmail", Accounts.class);
            query.setParameter("email", email);
        
        Accounts account = query.getSingleResult();

        if (account != null) {
            System.out.println(account);
            Profiles profile = account.getProfile();
            System.out.println(profile);
            return profile;
        }
        em.close();
        return null;
    }

    @Override
    public void updateProfile() {
        // TODO Auto-generated method stub
        
    }

    
    
}
