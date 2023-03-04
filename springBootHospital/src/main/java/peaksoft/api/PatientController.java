package peaksoft.api;

import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import peaksoft.entity.Appointment;
import peaksoft.entity.Patient;
import peaksoft.exeptions.NotFoundException;
import peaksoft.service.*;

/**
 * @author kurstan
 * @created at 17.02.2023 22:51
 */
@Controller
@RequestMapping("/{hospitalId}/patients")
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DepartmentService departmentService;
    private final HospitalService hospitalService;

    public PatientController(PatientService patientService, DoctorService doctorService, AppointmentService appointmentService, DepartmentService departmentService, HospitalService hospitalService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.departmentService = departmentService;
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public String getAll(@PathVariable Long hospitalId, Model model) {
        try {
            model.addAttribute("hospital", hospitalService.getById(hospitalId));
            model.addAttribute("patients", patientService.getAllByHospitalId(hospitalId));
            return "patient/patients";
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/new")
    public String createNewPatient(@PathVariable Long hospitalId, Model model) {
        model.addAttribute("patient", new Patient());
        return "patient/new";
    }

    @PostMapping("/save")
    public String savePatient(@PathVariable Long hospitalId,
                              @ModelAttribute("patient") @Valid Patient patient,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "patient/new";
        }
        try {
            patientService.save(hospitalId, patient);
            return "redirect:/{hospitalId}/patients";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("Email", "This email already exists in the database");
            return "patient/new";
        }
    }

    @GetMapping("/{patientId}/edit")
    private String getUpdateForm(@PathVariable Long hospitalId,
                                 @PathVariable Long patientId,
                                 Model model) {
        try {
            model.addAttribute("patient", patientService.getById(patientId));
            return "patient/update";
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @PostMapping ("/{patientId}/update")
    private String updatePatient(@PathVariable Long hospitalId,
                                 @PathVariable Long patientId,
                                 @ModelAttribute("patient") @Valid Patient patient,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "patient/update";
        }
        try {
            patientService.update(patientId, patient);
            return "redirect:/{hospitalId}/patients";
        } catch (PersistenceException e) {
            model.addAttribute("Email", "This email already exists in the database");
            return "patient/update";
        }
    }

    @GetMapping("/{patientId}/delete")
    public String deletePatient(@PathVariable Long hospitalId,
                                @PathVariable Long patientId) {
        patientService.delete(patientId);
        return "redirect:/{hospitalId}/patients";
    }

    @GetMapping("/{patientId}/appointments")
    public String getAppointments(@PathVariable Long hospitalId,
                                  @PathVariable Long patientId,
                                  Model model) {
        try {
            model.addAttribute("patient", patientService.getById(patientId));
            model.addAttribute("appointments", patientService.getAppointments(hospitalId, patientId));
            return "patient/appointments";
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/{patientId}/newApp")
    public String makeApp(@PathVariable Long hospitalId,
                          @PathVariable Long patientId,
                          Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getAll(hospitalId));
        model.addAttribute("departments", departmentService.getAll(hospitalId));
        return "patient/newApp";
    }

    @PostMapping("/{patientId}/saveApp")
    public String saveApp(@PathVariable Long hospitalId,
                          @PathVariable("patientId") Long patientId,
                          @ModelAttribute("appointment") @Valid Appointment appointment,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctors", doctorService.getAll(hospitalId));
            model.addAttribute("departments", departmentService.getAll(hospitalId));
            return "patient/newApp";
        }
        appointmentService.save(hospitalId, appointment);
        return "redirect:/{hospitalId}/patients/{patientId}/appointments";
    }

    @GetMapping("/{patientId}/{appointmentId}/delete")
    public String deleteApp(@PathVariable Long hospitalId,
                            @PathVariable Long patientId,
                            @PathVariable Long appointmentId) {
        appointmentService.delete(hospitalId, appointmentId);
        return "redirect:/{hospitalId}/patients/{patientId}/appointments";
    }
}
