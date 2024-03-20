package org.example.loanbusinessv3.model.seralized;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class LoanGuarantorId implements Serializable {
    private Long loan_id;
    private Long guarantor_id;

    public Long getLoanId() {
        return loan_id;
    }
    public void setLoanId(Long loanId) {
        this.loan_id = loanId;
    }
    public Long getGuarantorId() {
        return guarantor_id;
    }
    public void setGuarantorId(Long guarantorId) {
        this.guarantor_id = guarantorId;
    }
}
