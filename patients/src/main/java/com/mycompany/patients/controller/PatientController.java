package com.mycompany.patients.controller;

import com.mycompany.patients.entity.Patient;
import com.mycompany.patients.exceptions.PatientNotFoundException;
import com.mycompany.patients.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Api(value = "Gestion des patients")
@RestController
public class PatientController {
    @Autowired
    private PatientService patientService;

    @ApiOperation(value = "Rechercher un patient selon son id") // Swagger
    @ApiResponse(code = 404, message = "Patient with id {id} dosn't exist in database")
    @GetMapping(value = "/patient/{id}")
    public Patient getPatientById(@PathVariable("id") int id) {
        Patient patient = patientService.findById(id);
        if (Objects.isNull(patient)) {
            throw new PatientNotFoundException("Patient with id " + id + "dosn't exist in database");
        }
        return patient;
    }

    @GetMapping(value = "/patient")
    public  Patient getPatient(@RequestParam("firstname" )String firstname, @RequestParam("lastname") String lastname){

    }

    @GetMapping(value = "/patients")
    public List<Patient> findAllPatients(){

    }

    @PostMapping(value = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
    public Patient savePatient(@RequestBody Patient patient){

    }

    @DeleteMapping(value = "/patient/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable("id") int id){
        String response = patientService.deletePatient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PutMapping(value = "/patient/{id}")
    public Patient updatePatient(@PathVariable("id") int id,  @RequestBody Patient patient){
        return patientService.updatePatient(id, patient);
    }


    @GetMapping("/Accueil")
    public String index() {
        return "Bienvenue sur Mediscreen-Patients !";
    }

    /*@RequestMapping("/patient/list")
    public String home(Model model) {
        model.addAttribute("patients", patientRepository.findAll()); // patient : nom dans la page html
        return "patient/list";
    }*/

    /*@GetMapping("/patient/add")
    public String addPatientForm(Patient patient) {
        return "patient/add";
    }*/

    @PostMapping("/patient/validate") // Appelé après Post sur le formulaire de saisie
    public String validate(@Valid Patient patient, BindingResult result, Model model) {
        String firstnameToFind = patient.getFirstname();
        String lastnameToFind = patient.getLastname();
        Optional<Patient> patientToVerify = patientRepository.findByFirstnameEqualsIgnoreCaseAndLastnameEqualsIgnoreCase(firstnameToFind, lastnameToFind);

        if (patientToVerify.isPresent()) {
            return "redirect:/patient/patientAlreadyExist";
        }

        if (!result.hasErrors()) {
            patientRepository.save(patient);
            model.addAttribute("patient", patientRepository.findAll());
            return "redirect:/patient/list";
        } else {
            return "patient/add";
        }
    }

    @GetMapping("/patient/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // Si non trouvé (Java 11)
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));
        model.addAttribute("patient", patient);
        return "patient/update";
    }

    @PostMapping("/patient/update/{id}") // Appelé après Post sur le formulaire de saisie
    public String updatePatient(@PathVariable("id") Integer id, @Valid Patient patient,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "patient/update";
        }

        patientRepository.save(patient);
        model.addAttribute("patient", patientRepository.findAll());

        return "redirect:/patient/list";
    }

    @GetMapping("/patient/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));
        patientRepository.delete(patient);
        model.addAttribute("patient", patientRepository.findAll());
        return "redirect:/patient/list";
    }
}
