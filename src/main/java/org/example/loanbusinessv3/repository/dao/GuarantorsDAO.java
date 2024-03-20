package org.example.loanbusinessv3.repository.dao;

import java.util.List;

import org.example.loanbusinessv3.model.Guarantors;

import jakarta.persistence.NoResultException;

public interface GuarantorsDAO {
    void createGuarantors(List<Guarantors> guarantors) throws Exception;

    void updateGuarantors(List<Guarantors> guarantors) throws NoResultException;

    Guarantors findByEmail(String email) throws NoResultException;
}
