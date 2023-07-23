package com.mediscreen.patient.controller;

import com.mediscreen.patient.dto.PatientDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.mediscreen.patient.service.PatientService;

import java.util.List;

@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    private static Logger logger = LoggerFactory.getLogger(PatientController.class);

    /**
     * Get a patient by ID.
     *
     * @param id - the patient id
     * @return A PatientDto object
     */
    @Operation(summary = "Get a patient by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found patient", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
    })
    @GetMapping(value = "/patient/{id}")
    public PatientDTO getPatientById(@PathVariable Integer id) {
        logger.info("Patient " + id + " requested");
        return patientService.getPatientById(id);
    }

    /**
     * Get a patient by ID.
     *
     * @param family    - the patient family name
     * @param given     - the patient given name
     * @return A PatientDto object
     */
    @Operation(summary = "Get a patient by its fullname")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found patient", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
    })
    @GetMapping(value = "/patient")
    public PatientDTO getPatientByName(@RequestParam("family") String family,
                                       @RequestParam("given") String given) {
        logger.info("Patient " + family + given + " requested");
        return patientService.getPatientByName(family, given);
    }

    /**
     * Get all patients.
     *
     * @return A list of PatientDto object
     */
    @Operation(summary = "Get all patients ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all patient", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Patients not found", content = @Content)
    })
    @GetMapping(value = "/patient/all")
    public List<PatientDTO> getPatientList() {
        logger.info("List of Patients requested");
        return patientService.getAllPatients();
    }

    /**
     * Save a new patient.
     *
     * @param patientDTO    - the new patient to add
     * @return the response entity with the message
     */
    @Operation(summary = "Save a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient added successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Failed to add patient", content = @Content)
    })
    @PostMapping(value = "/patient/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> addPatient(@ModelAttribute PatientDTO patientDTO) {
        logger.info("Adding new patient: " + patientDTO.getGiven() + " " + patientDTO.getFamily());

        //Check if the patient is saved
        if (patientService.addPatient(patientDTO)) {
            logger.info("created patient: " + patientDTO.getGiven() + " " + patientDTO.getFamily());
            return ResponseEntity.status(HttpStatus.CREATED).body("Patient added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add patient");
        }
    }

    /**
     * Update an existing patient.
     *
     * @param id         - the patient id
     * @param patientDTO - the updated patient data
     * @param result     - the result of the request
     * @return the response entity with the message
     */
    @Operation(summary = "Update a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid patient data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to update patient", content = @Content)
    })
    @PutMapping(value = "/patient/update/{id}")
    public ResponseEntity<String> updatePatient(@PathVariable Integer id, @RequestBody @Valid PatientDTO patientDTO, BindingResult result) {
        logger.info("Updating patient with ID: " + id);

        // Check if DTO is valid
        if (result.hasErrors()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid patient data");

        // Update the patient
        if (patientService.updatePatient(id, patientDTO)) {
            logger.info("Updated patient with ID: " + id);
            return ResponseEntity.status(HttpStatus.OK).body("Patient updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update patient");
        }
    }

    /**
     * Delete a patient by ID.
     *
     * @param id - the patient id
     * @return the response entity with the message
     */
    @Operation(summary = "Delete a patient by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to delete patient", content = @Content)
    })
    @DeleteMapping(value = "/patient/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Integer id) {
        logger.info("Deleting patient with ID: " + id);

        if (patientService.deletePatient(id)) {
            logger.info("Deleted patient with ID: " + id);
            return ResponseEntity.status(HttpStatus.OK).body("Patient deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete patient");
        }
    }

}
