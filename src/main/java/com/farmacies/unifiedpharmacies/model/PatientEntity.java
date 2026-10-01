package com.farmacies.unifiedpharmacies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.farmacies.unifiedpharmacies.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "full_name", nullable = false, length = 80)
    private String fullName;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @JsonIgnore
    @Column(nullable = false, length = 60)
    private String password;

    @Column(name = "zip_code", nullable = false, length = 9)
    private String zipCode;

    @Column(nullable = false, length = 6)
    private String state;

    @Column(nullable = false, length = 18)
    private String city;

    @Column(nullable = false, length = 100)
    private String neighborhood;

    @Column(nullable = false, length = 80)
    private String street;

    @Column(nullable = false, length = 10)
    private String number;

    @Column(length = 100)
    private String complement;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PatientStatus status;

    // Patient 1:N Request
    @OneToMany(mappedBy = "patient")
    private List<RequestEntity> requests;
}
