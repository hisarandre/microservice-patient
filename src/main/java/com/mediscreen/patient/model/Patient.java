package com.mediscreen.patient.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "family_name")
    private String family;

    @Column(name = "given_name")
    private String given;

    @Column(name = "sex")
    private String sex;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

}