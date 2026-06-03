package org.example.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_HQL")
public class User {
    @Id
    @GeneratedValue
    private  int user_id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    @Column (name = "passwordHash")
    private String passwordHash;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    public User() {
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }



    public User(String name, String email, String passwordHash, LocalDateTime createdAt) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    @Override
    public String toString(){
        return name+"||"+email+"||"+createdAt;
    }
}
