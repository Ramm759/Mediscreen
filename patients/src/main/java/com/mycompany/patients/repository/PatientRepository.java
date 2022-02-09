package com.mycompany.patients.repository;

import com.mycompany.patients.entity.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient, Integer> {
    Optional<Patient> findByFirstnameEqualsIgnoreCaseAndLastnameEqualsIgnoreCase(String firstname, String lastname);

}