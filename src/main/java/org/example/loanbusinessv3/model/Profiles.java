package org.example.loanbusinessv3.model;

import jakarta.persistence.*;

@Entity
public class Profiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profile_id;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Accounts account;

    @Column(unique = true, nullable = false)
    private String full_name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;


    public Profiles(String full_name, String phone, String address) {
        this.full_name = full_name;
        this.phone = phone;
        this.address = address;
    }

    public Profiles() {
    }

    public Long getId() {
        return profile_id;
    }

    public void setId(Long profile_id) {
        this.profile_id = profile_id;
    }

    public Accounts getAccount() {
        return account;
    }

    public void setAccount(Accounts account) {
        this.account = account;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
