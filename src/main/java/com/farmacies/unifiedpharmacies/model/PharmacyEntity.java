package com.farmacies.unifiedpharmacies.model;

import com.farmacies.unifiedpharmacies.enums.PharmacyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pharmacies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;
    @Column(name = "legal_name", nullable = false, length = 80)
    private String legalName;
    @Column(name = "trade_name", nullable = false, length = 80)
    private String tradeName;
    // muda na tabela
    @Column(nullable = false, unique = true, length = 50)
    private String email;
    @Column(nullable = false, length = 20)
    private String phone;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PharmacyStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "pharmacy")
    private List<MedicationAvailabilityEntity> medicationAvailabilities;


    @OneToMany(mappedBy = "pharmacy")
    private List<RequestEntity> requests;


    @OneToMany(mappedBy = "pharmacy")
    private List<UserEntity> users;
}
