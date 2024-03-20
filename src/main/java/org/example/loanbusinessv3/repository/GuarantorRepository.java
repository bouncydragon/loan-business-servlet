package org.example.loanbusinessv3.repository;

import java.util.List;

import org.example.loanbusinessv3.model.Guarantors;
import org.example.loanbusinessv3.repository.dao.GuarantorsDAO;
import org.example.loanbusinessv3.util.EntityManagerUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceUnit;

public class GuarantorRepository implements GuarantorsDAO{
    @PersistenceUnit
    private EntityManagerFactory emf = EntityManagerUtil.createEntityManagerFactory();

    @Override
    public void createGuarantors(List<Guarantors> guarantors) throws Exception {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            for (Guarantors guarantor : guarantors) {
                try {
                    Guarantors guarantorExist = em.createNamedQuery("findByEmail", Guarantors.class)
                    .setParameter("email", guarantor.getEmail())
                    .getSingleResult();

                    if (guarantorExist != null) {
                        throw new Exception("Guarantor with email " + guarantor.getEmail() + " already exist.");
                    }
                } catch (NoResultException e) {
                    Guarantors guarantorData = new Guarantors(
                        guarantor.getFull_name(), 
                        guarantor.getEmail(), 
                        guarantor.getPhone()
                    );
                    em.persist(guarantorData);
                } catch (Exception e) {
                    em.getTransaction().rollback();
                    throw e;
                }
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void updateGuarantors(List<Guarantors> guarantors) throws NoResultException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Guarantors findByEmail(String email) throws NoResultException {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createNamedQuery("findByEmail", Guarantors.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    

    
}
