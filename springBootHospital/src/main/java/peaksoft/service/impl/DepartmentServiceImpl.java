package peaksoft.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import peaksoft.entity.Appointment;
import peaksoft.entity.Department;
import peaksoft.entity.Doctor;
import peaksoft.entity.Hospital;
import peaksoft.exeptions.NotFoundException;
import peaksoft.repositories.AppointmentRepository;
import peaksoft.repositories.DepartmentRepository;
import peaksoft.repositories.DoctorRepository;
import peaksoft.repositories.HospitalRepository;
import peaksoft.service.DepartmentService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kurstan
 * @created at 18.02.2023 11:05
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final HospitalRepository hospitalRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, HospitalRepository hospitalRepository) {
        this.departmentRepository = departmentRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public List<Department> getAll(Long id) {
        return departmentRepository.getAllByHospitalId(id);
    }

    @Override
    public void update(Long departmentId, Department department) {
        Department oldDepartment = departmentRepository
                .findById(departmentId).orElseThrow(
                        () -> new NotFoundException("Department by id " + departmentId + " is not found!"));
        oldDepartment.setName(department.getName());
    }

    @Override
    public void save(Long hospitalId, Department department) {
        Hospital hospital = hospitalRepository
                .findById(hospitalId).orElseThrow(
                        () -> new NotFoundException("Hospital by id " + hospitalId + " is not found!"));

        department.setHospital(hospital);
        departmentRepository.save(department);
    }

    @Override
    public void delete(Long departmentId) {
        Department department = departmentRepository
                .findById(departmentId).orElseThrow(
                        () -> new NotFoundException("Department by id " + departmentId + " not found"));

        List<Doctor> doctors = department.getDoctors();

        for (int i = 0; i < doctors.size(); i++) {
            doctors.get(i).getDepartments().remove(department);
        }

        Hospital hospital = department.getHospital();
        List<Appointment> appointments = appointmentRepository.getAllByHospitalId(hospital.getId());
        List<Appointment> depApp = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDepartment().getId().equals(departmentId)) {
                depApp.add(appointment);
            }
        }
        for (int i = 0; i < depApp.size(); i++) {
            depApp.get(i).getDoctor().getAppointments().clear();
            depApp.get(i).getPatient().getAppointments().clear();
        }
        hospital.getAppointments().removeAll(depApp);

        appointmentRepository.deleteAll(depApp);
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public Department findById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(
                        ()-> new NotFoundException("Department by id " + departmentId + " not found"));
    }

    @Override
    public List<Doctor> getDoctors(Long departmentId) {
        return departmentRepository.getDoctors(departmentId);
    }
}
