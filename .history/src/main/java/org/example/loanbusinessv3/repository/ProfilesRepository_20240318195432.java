package org.example.loanbusinessv3.repository;

import java.util.*;
import java.time.format.DateTimeFormatter;

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
    public Map<String, Object> selectProfile(String email) {
        EntityManager em = emf.createEntityManager();
    
        TypedQuery<Accounts> query = em.createNamedQuery("findAccountByEmail", Accounts.class);
            query.setParameter("email", email);
        
        Accounts account = query.getSingleResult();

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

            System.out.println(response);

            em.close();
            return response;
        }
        return null;
    }

    @Override
    public void updateProfile() {
        // TODO Auto-generated method stub
        
    }

    
    
}
