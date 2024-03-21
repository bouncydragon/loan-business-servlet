package org.example.loanbusinessv3.model;

import java.util.List;

import jakarta.persistence.*;

@NamedQuery(name = "findByEmail", query = "SELECT g FROM Guarantors g WHERE g.email = :email")

@Entity
@Table(name = "guarantors")
public class Guarantors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guarantor_id")
    private Long guarantor_id;

    @Column(unique = true, nullable = false)
    private String full_name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    // @ManyToMany(mappedBy = "guarantors")
    // private List<Loans> loans;

    // public List<Loans> getLoans() {
    //     return loans;
    // }

    // public void setLoans(List<Loans> loans) {
    //     this.loans = loans;
    // }

    public Guarantors(String full_name, String email, String phone) {
        this.full_name = full_name;
        this.email = email;
        this.phone = phone;
    }

    public Guarantors() {
    }

    public Long getGuarantor_id() {
        return guarantor_id;
    }

    public void setGuarantor_id(Long guarantor_id) {
        this.guarantor_id = guarantor_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
