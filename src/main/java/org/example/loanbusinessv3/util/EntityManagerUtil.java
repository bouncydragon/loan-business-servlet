package org.example.loanbusinessv3.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerUtil {
    private static final String PERSISTENCE_UNIT_NAME = "LoanBusiness";
    private static EntityManagerFactory emf;

    private EntityManagerUtil() {}

    public static synchronized EntityManagerFactory createEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return emf;
    }
}
