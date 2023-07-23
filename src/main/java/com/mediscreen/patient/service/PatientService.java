package com.mediscreen.patient.service;

import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.exception.PatientNotFoundException;
import com.mediscreen.patient.model.MapstructMapper;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.repository.PatientRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class PatientService  implements IPatientService  {
    @Autowired
    private PatientRepository patientRepository;

    MapstructMapper mapper = Mappers.getMapper(MapstructMapper.class);

    @Override
    public PatientDTO getPatientById (Integer id) {
        Optional<Patient> patient = patientRepository.findById(id);

        if (patient.isPresent()) {
            PatientDTO patientDTO = mapper.patientToDTO(patient.get());
            return patientDTO;
        }

        throw new PatientNotFoundException("Patient not found with ID: " + id);
    }

    @Override
    public PatientDTO getPatientByName (String family, String given) {
        Optional<Patient> patient = patientRepository.findByFamilyAndGiven(family, given);

        if (patient.isPresent()) {
            PatientDTO patientDTO = mapper.patientToDTO(patient.get());
            return patientDTO;
        }

        throw new PatientNotFoundException("Patient not found with name: " + family + given );
    }


    @Override
    public List<PatientDTO> getAllPatients() {
        try {
            List<Patient> patients = patientRepository.findAll();
            return mapper.patientListToDTO(patients);
        } catch (Exception e) {
            throw new PatientNotFoundException("Patients are not found.");
        }
    }

    @Override
    public boolean addPatient(PatientDTO patientDTO) {
        try {
            Patient patient = mapper.patientDTOToPatient(patientDTO);
            patientRepository.save(patient);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePatient(Integer id, PatientDTO patientDTO) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patientDTO.setId(id);
            patient = mapper.patientDTOToPatient(patientDTO);
            patientRepository.save(patient);
            return true;
        }

        throw new PatientNotFoundException("Patient not found with ID: " + id);
    }


    @Override
    public boolean deletePatient(Integer id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            patientRepository.deleteById(id);
            return true;
        }

        throw new PatientNotFoundException("Patient not found with ID: " + id);
    }
}
