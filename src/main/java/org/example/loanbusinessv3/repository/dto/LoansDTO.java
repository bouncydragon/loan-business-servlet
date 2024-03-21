package org.example.loanbusinessv3.repository.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.example.loanbusinessv3.model.Status;

public class LoansDTO {
    private Long loan_id;
    private double loan_amount;
    private double interest_rate;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Status status;
    private List<GuarantorsDTO> guarantors;

    public Long getLoan_id() {
        return loan_id;
    }
    public void setLoan_id(Long loan_id) {
        this.loan_id = loan_id;
    }
    public double getLoan_amount() {
        return loan_amount;
    }
    public void setLoan_amount(double loan_amount) {
        this.loan_amount = loan_amount;
    }
    public double getInterest_rate() {
        return interest_rate;
    }
    public void setInterest_rate(double interest_rate) {
        this.interest_rate = interest_rate;
    }
    public LocalDateTime getStart_date() {
        return start_date;
    }
    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }
    public LocalDateTime getEnd_date() {
        return end_date;
    }
    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public List<GuarantorsDTO> getGuarantors() {
        return guarantors;
    }
    public void setGuarantors(List<GuarantorsDTO> guarantors) {
        this.guarantors = guarantors;
    }
}
