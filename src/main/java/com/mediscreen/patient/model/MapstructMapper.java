package com.mediscreen.patient.model;

import com.mediscreen.patient.dto.PatientDTO;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper
public interface MapstructMapper {

    List<PatientDTO> patientListToDTO(List<Patient> patients);
    PatientDTO patientToDTO(Patient patient);
    Patient patientDTOToPatient(PatientDTO patientDTO);
}
