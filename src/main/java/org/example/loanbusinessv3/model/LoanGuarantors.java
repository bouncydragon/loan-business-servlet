package org.example.loanbusinessv3.model;

import org.example.loanbusinessv3.model.seralized.LoanGuarantorId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan_guarantors")
public class LoanGuarantors {

    @EmbeddedId
    private LoanGuarantorId guarantor_id;
}
