package com.mediscreen.patient.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.mediscreen.patient.controller.PatientController;
import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.service.PatientService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import javax.sql.DataSource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@TestExecutionListeners(
        listeners = {DependencyInjectionTestExecutionListener.class, SqlScriptsTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
class PatientControllerTest {
    @Mock
    private PatientService patientService;
    @InjectMocks
    private PatientController patientController;
    @Mock
    private BindingResult bindingResult;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    public MockMvc mvc;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @BeforeEach
    public void setupData() {
        Resource schemaScript = new ClassPathResource("1_schema_test.sql");
        Resource dataScript = new ClassPathResource("2_data_test.sql");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(schemaScript, dataScript);
        populator.execute(dataSource);
    }

    @Test
    void testGetPatientById() throws Exception {
        // GIVEN
        // There are already patients in db
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1);

        // WHEN
        // I request the info about a specific patient
        when(patientService.getPatientById(1)).thenReturn(patientDTO);
        PatientDTO result = patientService.getPatientById(1);

        // THEN
        // I get the information about the patient I requested
        mvc.perform(get("/patient/1")).andExpect(status().isOk());
        assertNotNull(result);
    }

    @Test
    void testGetPatientByName() throws Exception {
        // GIVEN
        // There are already patients in db
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1);

        // WHEN
        // I request the info about a specific patient
        when(patientService.getPatientByName("Jones","Sophia")).thenReturn(patientDTO);
        PatientDTO result = patientService.getPatientById(1);

        // THEN
        // I get the information about the patient I requested
        mvc.perform(get("/patient?family=Jones&given=Sophia")).andExpect(status().isOk());
    }

    @Test
    void testGetPatientList() throws Exception {
        // GIVEN
        // There are already patients in db
        List<PatientDTO> patientList = new ArrayList<>();


        // WHEN
        // I request the list of patients created
        when(patientService.getAllPatients()).thenReturn(patientList);

        // THEN
        mvc.perform(get("/patient/all")).andExpect(status().isOk());
    }

    @Test
    void testAddPatient() throws Exception {
        // GIVEN
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("family", "Smith");
        formData.add("given", "John");
        formData.add("sex", "M");
        formData.add("dob", "2000-10-10");
        formData.add("address", "123 Main Street");
        formData.add("phone", "555-123-4567");

        // WHEN
        when(patientService.addPatient(any(PatientDTO.class))).thenReturn(true);

        // THEN
        mvc.perform(post("/patient/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(formData))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddPatient_InvalidData() throws Exception {
        // GIVEN
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        // WHEN
        when(patientService.addPatient(any(PatientDTO.class))).thenReturn(false);

        // THEN
        mvc.perform(post("/patient/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(formData))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testUpdatePatient() throws Exception {
        // GIVEN
        // There is a patient to update with valid data
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setGiven("Jon");
        patientDTO.setFamily("Doe");
        patientDTO.setPhone("000-000-000");
        patientDTO.setSex("M");
        patientDTO.setAddress("An address here");
        patientDTO.setDob(LocalDate.ofEpochDay(2000-10-10));

        // WHEN
        // I update the infos
        when(bindingResult.hasErrors()).thenReturn(false);
        when(patientService.updatePatient(eq(1), any(PatientDTO.class))).thenReturn(true);

        // THEN
        // it shouldn't update the patient
        mvc.perform(put("/patient/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePatient_InvalidData() throws Exception {
        // GIVEN
        // There is a patient to update with invalid data
        PatientDTO patientDTO = new PatientDTO();

        // WHEN
        // I update the infos
        when(bindingResult.hasErrors()).thenReturn(true);

        // THEN
        // it shouldn't update the patient
        mvc.perform(put("/patient/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeletePatient() throws Exception {
        // WHEN
        // I delete a patient
        when(patientService.deletePatient(1)).thenReturn(true);

        // THEN
        // it shouldn't update the patient
        mvc.perform(delete("/patient/1")).andExpect(status().isOk());
    }
}
