package org.example.loanbusinessv3.model;

import com.google.gson.annotations.JsonAdapter;
import jakarta.persistence.*;
import org.example.loanbusinessv3.util.LocalDateTypeAdapter;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int account_id;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonAdapter(LocalDateTypeAdapter.class)
    @Column(nullable = false)
    private LocalDateTime created_at;

    @OneToOne(mappedBy = "account_id", cascade = CascadeType.PERSIST)
    private Profiles profile;

    public Accounts(String email) {
        this.email = email;
        this.created_at = LocalDateTime.now();
    }

    public Accounts() {

    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Profiles getProfile() {
        return profile;
    }

    public void setProfile(Profiles profile) {
        this.profile = profile;
    }
}
