package com.group15.TUKulinarium.models;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "refreshtoken")
@Table(name = "refreshtoken")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "refreshtoken_user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "refreshtoken_token")
    private String token;

    @Column(name = "refreshtoken_expiry_date")
    private Instant expiryDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}
