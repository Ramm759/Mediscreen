package com.mycompany.patients.controller;

import com.mycompany.patients.entity.Patient;
import com.mycompany.patients.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
        if (!result.hasErrors()) {
            patientRepository.save(patient);
            model.addAttribute("patient", patientRepository.findAll());
            return "redirect:/patient/list";
        }
        return "patient/add";
    }

    @GetMapping("/patient/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // Si non trouvé (Java 11)
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));
        model.addAttribute("patient", patient);
        return "patient/update";
    }

    @PostMapping("/patient/update/{id}")
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
