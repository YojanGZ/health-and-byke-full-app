package com.healthandbyke.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    
    @Column(nullable = false, length = 100)
    private String lastName;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(length = 20)
    private String phoneNumber;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(length = 50)
    private String typeBike;

    public User() {}

    public User(String lastName, String name, String email, String phoneNumber, LocalDate date, String typeBike) {
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.typeBike = typeBike;
    }

    // Getters y Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getTypeBike() { return typeBike; }
    public void setTypeBike(String typeBike) { this.typeBike = typeBike; }
}
