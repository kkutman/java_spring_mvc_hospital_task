package peaksoft.api;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import peaksoft.entity.Hospital;
import peaksoft.exeptions.NotFoundException;
import peaksoft.service.*;

/**
 * @author kurstan
 * @created at 17.02.2023 12:47
 */
@Controller
@RequestMapping("/hospitals")
public class HospitalController {
    private final HospitalService hospitalService;
    private final PatientService patientService;
    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    public HospitalController(HospitalService hospitalService, PatientService patientService, DepartmentService departmentService, DoctorService doctorService, AppointmentService appointmentService) {
        this.hospitalService = hospitalService;
        this.patientService = patientService;
        this.departmentService = departmentService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String getAll(@RequestParam(name = "keyWord", required = false) String keyWord,
                         Model model) {
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("hospitals", hospitalService.getAllHospitals(keyWord));
        return "hospital/main";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long hospitalId, Model model) {
        int countPat = patientService.getAllByHospitalId(hospitalId).size();
        int countDep = departmentService.getAll(hospitalId).size();
        int countDoc = doctorService.getAll(hospitalId).size();
        int countApp = appointmentService.getAll(hospitalId).size();

        try {
            model.addAttribute("countPat", countPat);
            model.addAttribute("countDep", countDep);
            model.addAttribute("countDoc", countDoc);
            model.addAttribute("countApp", countApp);
            model.addAttribute("hospital", hospitalService.getById(hospitalId));
            return "hospital/home";
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/new")
    public String createNewHospital(Model model) {
        model.addAttribute("hospital", new Hospital());
        return "hospital/new";
    }

    @PostMapping("/save")
    public String saveHospital(@ModelAttribute("hospital") @Valid Hospital hospital,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "hospital/new";
        }
        hospitalService.save(hospital);
        return "redirect:/hospitals";
    }

    @GetMapping("/{id}/delete")
    public String deleteHospital(@PathVariable Long id) {
        hospitalService.delete(id);
        return "redirect:/hospitals";
    }

    @GetMapping("/{id}/edit")
    public String getUpdateForm(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute("hospital", hospitalService.getById(id));
            return "hospital/update";
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/{id}/update")
    public String updateHospital(@PathVariable("id") Long id,
                                 @ModelAttribute("hospital") @Valid Hospital hospital,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "hospital/update";
        }
        hospitalService.update(id, hospital);
        return "redirect:/hospitals";
    }
}
