package org.example.loanbusinessv3.repository;

import java.util.List;

import org.example.loanbusinessv3.model.Guarantors;
import org.example.loanbusinessv3.model.Loans;
import org.example.loanbusinessv3.repository.dao.GuarantorsDAO;
import org.example.loanbusinessv3.util.EntityManagerUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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
    public void updateGuarantors(List<Guarantors> guarantorToUpdate) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
    
            for (Guarantors guarantor : guarantorToUpdate) {
                Guarantors existingGuarantor = em.find(Guarantors.class, guarantor.getGuarantor_id());
                if (existingGuarantor == null) {
                    System.out.println("Guarantor does not exist.");
                    em.getTransaction().rollback();
                    return;
                }
    
                existingGuarantor.setEmail(guarantor.getEmail());
                existingGuarantor.setFull_name(guarantor.getFull_name());
                existingGuarantor.setPhone(guarantor.getPhone());
    
                em.merge(existingGuarantor);
            }
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

    @Override
    public List<Guarantors> findAllGuarantors() {
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Guarantors> cq = cb.createQuery(Guarantors.class);
        Root<Guarantors> root = cq.from(Guarantors.class);
        CriteriaQuery<Guarantors> recs = cq.select(root);
        return em.createQuery(recs).getResultList();
    }

    @Override
    public void removeGuarantor(String guarantorId) {
        EntityManager em = emf.createEntityManager();
        
        Guarantors guarantor = em.find(Guarantors.class, guarantorId);
        try {
            em.getTransaction().begin();
            if (guarantor != null) {
                em.remove(guarantor);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        
    }

    
}
