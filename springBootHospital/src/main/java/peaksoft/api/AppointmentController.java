package peaksoft.api;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import peaksoft.entity.Appointment;
import peaksoft.exeptions.BadRequestException;
import peaksoft.exeptions.NotFoundException;
import peaksoft.service.*;

/**
 * @author kurstan
 * @created at 18.02.2023 12:23
 */
@Controller
@RequestMapping("/{hospitalId}/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DepartmentService departmentService;
    private final HospitalService hospitalService;

    public AppointmentController(AppointmentService appointmentService, DoctorService doctorService, PatientService patientService, DepartmentService departmentService, HospitalService hospitalService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.departmentService = departmentService;
        this.hospitalService = hospitalService;
    }
    @GetMapping
    public String getAll(@PathVariable Long hospitalId, Model model){
        model.addAttribute("hospital", hospitalService.getById(hospitalId));
        model.addAttribute("appointments", appointmentService.getAll(hospitalId));
        return "appointment/appointments";
    }
    @GetMapping("/new")
    public String create(@PathVariable Long hospitalId, Model model){
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getAll(hospitalId));
        model.addAttribute("patients", patientService.getAllByHospitalId(hospitalId));
        model.addAttribute("departments", departmentService.getAll(hospitalId));
        return "appointment/new";
    }
    @PostMapping("/save")
    public String save(@PathVariable Long hospitalId,
                       @ModelAttribute("appointment")@Valid Appointment appointment,
                       BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("doctors", doctorService.getAll(hospitalId));
            model.addAttribute("patients", patientService.getAllByHospitalId(hospitalId));
            model.addAttribute("departments", departmentService.getAll(hospitalId));
            return "appointment/new";
        }
        try {
            model.addAttribute("doctors", doctorService.getAll(hospitalId));
            model.addAttribute("patients", patientService.getAllByHospitalId(hospitalId));
            model.addAttribute("departments", departmentService.getAll(hospitalId));
            appointmentService.save(hospitalId, appointment);
            return "redirect:/{hospitalId}/appointments";
        } catch (BadRequestException e) {
            model.addAttribute("depError", "Doctor doesn't assign this department!");
            return "appointment/new";
        }
    }
    @GetMapping("/{appointmentId}/edit")
    public String getUpdateForm(@PathVariable Long hospitalId,
                                @PathVariable Long appointmentId,
                                Model model){
        try {
            model.addAttribute("appointment", appointmentService.findById(appointmentId));
            model.addAttribute("doctors", doctorService.getAll(hospitalId));
            model.addAttribute("patients", patientService.getAllByHospitalId(hospitalId));
            model.addAttribute("departments", departmentService.getAll(hospitalId));
        } catch (NotFoundException e){
            return e.getMessage();
        }
        return "appointment/update";
    }
    @PostMapping("/{appointmentId}/update")
    public String update(@PathVariable Long hospitalId,
                         @PathVariable Long appointmentId,
                         @ModelAttribute("appointment") @Valid Appointment appointment,
                         BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("doctors", doctorService.getAll(hospitalId));
            model.addAttribute("patients", patientService.getAllByHospitalId(hospitalId));
            model.addAttribute("departments", departmentService.getAll(hospitalId));
            return "appointment/update";
        }
        try {
            model.addAttribute("doctors", doctorService.getAll(hospitalId));
            model.addAttribute("patients", patientService.getAllByHospitalId(hospitalId));
            model.addAttribute("departments", departmentService.getAll(hospitalId));
            appointmentService.update(appointmentId, appointment);
            return "redirect:/{hospitalId}/appointments";
        } catch (BadRequestException e) {
            model.addAttribute("depError", "Doctor doesn't assign this department!");
            return "appointment/update";
        }
    }
    @GetMapping("/{appointmentId}/delete")
    public String delete(@PathVariable Long hospitalId,
                         @PathVariable Long appointmentId){
        appointmentService.delete(hospitalId, appointmentId);
        return "redirect:/{hospitalId}/appointments";
    }
}
