package com.mediscreen.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDTO {

    private Integer id;

    @NotBlank(message = "Family name is mandatory")
    private String family;

    @NotBlank(message = "Given name is mandatory")
    private String given;

    @NotBlank(message = "Sex is mandatory")
    private String sex;

    @NotNull(message = "Date of birth is mandatory")
    private LocalDate dob;

    private String address;

    private String phone;

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", familyName='" + family + '\'' +
                ", givenName=" + given + '\'' +
                ", sex=" + sex + '\'' +
                ", dateOfBirth=" + dob + '\'' +
                ", address=" + address  + '\'' +
                ", phone=" + phone +
                "}";
    }

}