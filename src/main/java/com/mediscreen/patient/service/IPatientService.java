package com.mediscreen.patient.service;

import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.exception.PatientNotFoundException;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing patients.
 */
public interface IPatientService {

    /**
     * Get a patient by ID.
     *
     * @param id the patient ID
     * @return the patient DTO
     * @throws PatientNotFoundException if the patient is not found
     */
    PatientDTO getPatientById(Integer id) throws PatientNotFoundException;

    /**
     * Get a patient by its fullname.
     *
     * @param family the patient family name
     * @param given the patient given name
     * @return the patient DTO
     * @throws PatientNotFoundException if the patient is not found
     */
    PatientDTO getPatientByName(String family, String given) throws PatientNotFoundException;

    /**
     * Get all patients.
     *
     * @return a list of patient DTOs
     * @throws PatientNotFoundException if no patients are found
     */
    List<PatientDTO> getAllPatients() throws PatientNotFoundException;

    /**
     * Add a new patient.
     *
     * @param patientDTO the patient DTO to add
     * @return true if the patient is added successfully, false otherwise
     */
    boolean addPatient(PatientDTO patientDTO);

    /**
     * Update an existing patient.
     *
     * @param id         the ID of the patient to update
     * @param patientDTO the updated patient DTO
     * @return true if the patient is updated successfully, false otherwise
     * @throws PatientNotFoundException if the patient is not found
     */
    boolean updatePatient(Integer id, PatientDTO patientDTO) throws PatientNotFoundException;

    /**
     * Delete a patient by ID.
     *
     * @param id the ID of the patient to delete
     * @return true if the patient is deleted successfully, false otherwise
     * @throws PatientNotFoundException if the patient is not found
     */
    boolean deletePatient(Integer id) throws PatientNotFoundException;
}