package com.mycompany.patients.repository;

import com.mycompany.patients.entity.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Integer> {
    Optional<Patient> findByFirstnameEqualsIgnoreCaseAndLastnameEqualsIgnoreCase(String firstname, String lastname);

    Patient findById(int id);
}