package com.mycompany.patients.service;

import com.mycompany.patients.entity.Patient;
import com.mycompany.patients.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    PatientRepository patientRepository;
    public Patient findById(int id) {
        return patientRepository.findById(id);
    }
}
