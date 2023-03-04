package peaksoft.api;

import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import peaksoft.entity.Doctor;
import peaksoft.exeptions.ExistsInDataBase;
import peaksoft.exeptions.NotFoundException;
import peaksoft.service.DepartmentService;
import peaksoft.service.DoctorService;
import peaksoft.service.HospitalService;

/**
 * @author kurstan
 * @created at 18.02.2023 4:07
 */
@Controller
@RequestMapping("/{hospitalId}/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final HospitalService hospitalService;

    public DoctorController(DoctorService doctorService, DepartmentService departmentService, HospitalService hospitalService) {
        this.doctorService = doctorService;
        this.departmentService = departmentService;
        this.hospitalService = hospitalService;
    }

    @GetMapping()
    public String getAll(@PathVariable Long hospitalId, Model model) {
        try {
            model.addAttribute("hospital", hospitalService.getById(hospitalId));
            model.addAttribute("doctors", doctorService.getAll(hospitalId));
            return "doctor/doctors";
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/new")
    public String createNewDoctor(@PathVariable Long hospitalId, Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctor/new";
    }

    @PostMapping("/save")
    public String save(@PathVariable Long hospitalId,
                       @ModelAttribute("doctor") @Valid Doctor doctor,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            return "doctor/new";
        }
        try {
            doctorService.save(hospitalId, doctor);
            return "redirect:/{hospitalId}/doctors";
        } catch (ExistsInDataBase e) {
            model.addAttribute("Email", "This email already exists in the database");
            return "doctor/new";
        }
    }

    @GetMapping("/{doctorId}/edit")
    public String getUpdateFrom(@PathVariable Long hospitalId,
                                @PathVariable("doctorId") Long doctorId,
                                Model model) {
        try {
            model.addAttribute("doctor", doctorService.findById(doctorId));
            return "doctor/update";
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/{doctorId}/update")
    public String update(@PathVariable Long hospitalId,
                         @PathVariable("doctorId") Long doctorId,
                         @ModelAttribute("doctor") @Valid Doctor doctor,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "doctor/update";
        }
        try {
            doctorService.update(doctorId, doctor);
            return "redirect:/{hospitalId}/doctors";
        } catch (ExistsInDataBase e) {
            model.addAttribute("Email", "This email already exists in the database");
            return "doctor/update";
        }
    }

    @GetMapping("/{doctorId}/delete")
    public String delete(@PathVariable Long hospitalId,
                         @PathVariable("doctorId") Long doctorId) {
        doctorService.delete(doctorId);
        return "redirect:/{hospitalId}/doctors";
    }

    @GetMapping("/{doctorId}/departments")
    public String getDepartments(@PathVariable Long hospitalId,
                                 @PathVariable("doctorId") Long doctorId,
                                 Model model) {
        try {
            model.addAttribute("doctor", doctorService.findById(doctorId));
            model.addAttribute("departments", doctorService.getDepartments(doctorId));
            return "doctor/departments";
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/{doctorId}/newDepartment")
    public String assignToDepartment(@PathVariable Long hospitalId,
                                     @PathVariable("doctorId") Long doctorId,
                                     Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("departments", doctorService.getCanBeAssignDepartments(doctorId));
        return "doctor/assignToDepartment";
    }

    @PostMapping("/{doctorId}/saveDepartment")
    public String saveDepartment(@PathVariable Long hospitalId,
                                 @PathVariable("doctorId") Long doctorId,
                                 @ModelAttribute("doctor") Doctor doctor) {
        doctorService.assignToDepartment(doctorId, doctor);
        return "redirect:/{hospitalId}/doctors/{doctorId}/departments";
    }

    @GetMapping("/{doctorId}/{departmentId}/delete")
    public String deleteDepartment(@PathVariable Long hospitalId,
                                   @PathVariable("doctorId") Long doctorId,
                                   @PathVariable("departmentId") Long departmentId) {
        doctorService.deleteDepartment(doctorId, departmentId);
        return "redirect:/{hospitalId}/doctors/{doctorId}/departments";
    }

    @GetMapping("/{doctorId}/appointments")
    public String getAppointments(@PathVariable Long hospitalId,
                                  @PathVariable("doctorId") Long doctorId,
                                  Model model) {
        try {
            model.addAttribute("doctor", doctorService.findById(doctorId));
            model.addAttribute("appointments", doctorService.getAppointments(doctorId));
            return "doctor/appointments";
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }
}
