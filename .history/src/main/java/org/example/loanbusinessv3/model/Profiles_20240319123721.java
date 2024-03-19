package org.example.loanbusinessv3.model;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import jakarta.persistence.*;

@Entity
public class Profiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "profile_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Accounts account_id;

    @Expose
    @Column(unique = true, nullable = false)
    private String full_name;

    @Expose
    @Column(nullable = false)
    private String phone;

    @Expose
    @Column(nullable = false)
    private String address;

    @Override
    public String toString() {
        return "model.Profiles{" +
                "account_id=" + account_id +
                ", full_name='" + full_name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public Profiles() {
    }

    public Profiles(String full_name, String phone, String address) {
        this.full_name = full_name;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Accounts getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Accounts account_id) {
        this.account_id = account_id;
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
