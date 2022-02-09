package com.mycompany.patients.controller;

import com.mycompany.patients.entity.BidList;
import com.mycompany.patients.entity.Patient;
import com.mycompany.patients.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Optional;

@Controller
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/Accueil")
    public String index() {
        return "Greetings from Mediscreen-Patients !";
    }

    @RequestMapping("/patient/list")
    public String home(Model model) {
        model.addAttribute("patients", patientRepository.findAll()); // patient : nom dans la page html
        return "patient/list";
    }

    @GetMapping("/patient/add")
    public String addPatientForm(Patient patient) {
        return "patient/add";
    }

    @PostMapping("/patient/validate") // Appelé après Post sur le formulaire de saisie
    public String validate(@Valid Patient patient, BindingResult result, Model model) {
        // BindingResult regroupe les erreurs
        if (!result.hasErrors()){
            patientRepository.save(patient);
            model.addAttribute("patient", patientRepository.findAll());
            return "redirect:/patient/list";
        }
        return "patient/add";
    }



    /*@PostMapping(value = "/patients")
    public ResponseEntity addPatient(@RequestBody @Valid Patient patient) {
        String firstnameToSearch = patient.getFirstname();
        String lastnameToSearch = patient.getLastname();
        Optional<Patient> patientAlreadyExists = patientRepository.findByFirstnameEqualsIgnoreCaseAndLastnameEqualsIgnoreCase(firstnameToSearch, lastnameToSearch);

        if (patientAlreadyExists.isPresent()) {
            return new ResponseEntity("Patient déjà existant", HttpStatus.BAD_REQUEST);
        }

        patientRepository.save(patient);
        return new ResponseEntity(patient, HttpStatus.CREATED);
    }*/

    @DeleteMapping(value = "/patient/{patientId}")
    public ResponseEntity deletePatient(@PathVariable("patientId") Integer patientId) {
        Optional<Patient> patientToDelete = patientRepository.findById(patientId);

        if (!patientToDelete.isPresent()) {
            return new ResponseEntity("Patient non trouvé", HttpStatus.BAD_REQUEST);
        }

        patientRepository.delete(patientToDelete.get());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/patient/{patientId}")
    public ResponseEntity updatePatient(@PathVariable("patientId") Integer patientId, @RequestBody @Valid Patient patient) {

        Optional<Patient> patientAlreadyExists = patientRepository.findById(patientId);
        if (!patientAlreadyExists.isPresent()) {
            return new ResponseEntity("Patient inexistant", HttpStatus.BAD_REQUEST);
        }

        Patient patientToUpdate = patientAlreadyExists.get();

        patientToUpdate.setFirstname(patient.getFirstname());
        patientToUpdate.setLastname(patient.getLastname());
        patientToUpdate.setDateOfBirth(patient.getDateOfBirth());
        patientToUpdate.setGenre(patient.getGenre());
        patientToUpdate.setAdress(patient.getAdress());
        patientToUpdate.setAdressCity(patient.getAdressCity());
        patientToUpdate.setAdressCp(patient.getAdressCp());

        patientRepository.save(patientToUpdate);
        return new ResponseEntity(patientToUpdate, HttpStatus.CREATED);
    }
}
