package org.example.loanbusinessv3.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import org.example.loanbusinessv3.util.LocalDateTypeAdapter;

import com.google.gson.annotations.JsonAdapter;

@Entity
@Table(name = "loans")
public class Loans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long loan_id;

    @ManyToOne
    @JoinColumn(name = "account_id", updatable = false)
    private Accounts account_id;

    @ManyToMany
    @JoinTable(
            name = "loan_guarantors",
            joinColumns = @JoinColumn(name = "loan_id"),
            inverseJoinColumns = @JoinColumn(name = "guarantor_id")
    )
    private List<Guarantors> guarantors;

    @Column(nullable = false)
    private double loan_amount;

    @Column(nullable = false)
    private double interest_rate;

    @JsonAdapter(LocalDateTypeAdapter.class)
    @Column(nullable = false)
    private LocalDateTime start_date;

    @JsonAdapter(LocalDateTypeAdapter.class)
    @Column(nullable = false)
    private LocalDateTime end_date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public Loans() {
    }

    public Loans(double loan_amount, double interest_rate, Status status) {
        this.loan_amount = loan_amount;
        this.interest_rate = interest_rate;
        this.start_date = LocalDateTime.now();
        this.end_date = LocalDateTime.now().plusMonths(24);
        this.status = status;
    }

    public Long getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(Long loan_id) {
        this.loan_id = loan_id;
    }

    public Accounts getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Accounts account_id) {
        this.account_id = account_id;
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

    public List<Guarantors> getGuarantors() {
        return guarantors;
    }

    public void setGuarantors(List<Guarantors> guarantors) {
        this.guarantors = guarantors;
    }
}
