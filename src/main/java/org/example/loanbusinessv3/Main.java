package org.example.loanbusinessv3;

import jakarta.persistence.*;
import org.example.loanbusinessv3.model.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("loanBusiness");
        EntityManager em = emf.createEntityManager();

        Accounts account = new Accounts("dev-test-00@yahoo.com");
        Profiles newProfile = new Profiles("Dev Duane", "+6398728736", "SF. PAMP.");
        Loans newLoan = new Loans(100_000.00, 0.5, Status.WAITING);
        Guarantors newGuarantor = new Guarantors("Karla L.", "k.lazating@gmail.com", "09778364738");
        Guarantors newGuarantor2 = new Guarantors("Karla C.", "k.cusi@gmail.com", "09778364738");

        em.getTransaction().begin();

            em.persist(account);

        System.out.println("Account has been created!");

            newProfile.setAccount_id(account);
            account.setProfile(newProfile);

        System.out.println("Profile has been created!");

            newLoan.setAccount_id(account);
            em.persist(newLoan);

            em.persist(newGuarantor);
            em.persist(newGuarantor2);

            newLoan.setGuarantors(List.of(newGuarantor, newGuarantor2));

        em.getTransaction().commit();
        em.close();

    }
}
