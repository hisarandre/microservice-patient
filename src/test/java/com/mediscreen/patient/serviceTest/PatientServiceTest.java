package com.mediscreen.patient.serviceTest;

import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.exception.PatientNotFoundException;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.repository.PatientRepository;
import com.mediscreen.patient.service.PatientService;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    public void testGetPatientById() {
        // GIVEN
        // There are already a patient in db
        Integer patientId = 1;
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setGiven("John");
        patient.setFamily("Doe");
        Optional<Patient> optionalPatient = Optional.of(patient);

        // WHEN
        // I want the info about a patient
        when(patientRepository.findById(patientId)).thenReturn(optionalPatient);
        PatientDTO result = patientService.getPatientById(patientId);

        // THEN
        // It should return the patient info
        assertNotNull(result);
        assertEquals(patientId, result.getId());
        assertEquals("John", result.getGiven());
        assertEquals("Doe", result.getFamily());
    }

    @Test
    public void testGetPatientById_NoExistingPatient() {
        // GIVEN
        // There is no patient in the db
        Integer patientId = 1;
        Optional<Patient> optionalPatient = Optional.empty();

        // WHEN
        // I want the info about a patient
        when(patientRepository.findById(patientId)).thenReturn(optionalPatient);

        // THEN
        // It should throw an exception
        assertThrows(PatientNotFoundException.class, () -> {
            patientService.getPatientById(patientId);
        });
    }

    @Test
    public void testAddPatient() {
        // GIVEN
        // There is a patient with valid data
        PatientDTO patientDTO= new PatientDTO();
        patientDTO.setFamily("Smith");
        patientDTO.setGiven("John");
        patientDTO.setSex("M");
        patientDTO.setDob(LocalDate.ofEpochDay(2000-10-10));
        patientDTO.setAddress("123 Main Street");
        patientDTO.setPhone("555-123-4567");

        Patient patient = new Patient();

        // WHEN
        // I add a new patient
        when(patientRepository.save(patient)).thenReturn(new Patient());
        boolean result = patientService.addPatient(patientDTO);

        // THEN
        // it should be created
        assertTrue(result);
    }

    @Test
    public void testUpdatePatient() {
        // GIVEN
        // I send the new info with valid and there are patients in the db
        Integer patientId = 1;
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setGiven("John");
        patientDTO.setFamily("Doe");
        Optional<Patient> optionalPatient = Optional.of(new Patient());
        Patient patient = new Patient();

        // WHEN
        // I update a patient by its ID
        when(patientRepository.findById(patientId)).thenReturn(optionalPatient);
        when(patientRepository.save(patient)).thenReturn(new Patient());
        boolean result = patientService.updatePatient(patientId, patientDTO);

        // THEN
        // It should be updated
        assertTrue(result);
    }

    @Test
    public void testUpdatePatient_FailToUpdate() {
        // GIVEN
        // I send the new info with valid and there are patients in the db
        Integer patientId = 1;
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setGiven("John");
        patientDTO.setGiven("Doe");
        Optional<Patient> optionalPatient = Optional.empty();

        // WHEN
        // I update a patient by its ID
        when(patientRepository.findById(patientId)).thenReturn(optionalPatient);

        // THEN
        // It shouldn't be updated and I get an error
        assertThrows(PatientNotFoundException.class, () -> {
            patientService.updatePatient(patientId, patientDTO);
        });
    }

    @Test
    public void testDeletePatient() {
        // GIVEN
        // There are patient in db
        Integer patientId = 1;
        Optional<Patient> optionalPatient = Optional.of(new Patient());

        // WHEN
        // I delete a patient by its ID
        when(patientRepository.findById(patientId)).thenReturn(optionalPatient);
        boolean result = patientService.deletePatient(patientId);

        // THEN
        // It should be deleted
        assertTrue(result);
    }

    @Test
    public void testDeletePatient_NonExistingPatient_ShouldThrowException() {
        // GIVEN
        // There is no patient in the db
        Integer patientId = 1;
        Optional<Patient> optionalPatient = Optional.empty();

        // WHEN
        // I delete a patient by its ID
        when(patientRepository.findById(patientId)).thenReturn(optionalPatient);

        // THEN
        // It shouldn't be deleted
        assertThrows(PatientNotFoundException.class, () -> {
            patientService.deletePatient(patientId);
        });
    }

}
