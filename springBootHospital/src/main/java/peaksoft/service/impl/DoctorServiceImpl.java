package peaksoft.service.impl;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import peaksoft.entity.Appointment;
import peaksoft.entity.Department;
import peaksoft.entity.Doctor;
import peaksoft.entity.Hospital;
import peaksoft.exeptions.ExistsInDataBase;
import peaksoft.exeptions.NotFoundException;
import peaksoft.repositories.AppointmentRepository;
import peaksoft.repositories.DepartmentRepository;
import peaksoft.repositories.DoctorRepository;
import peaksoft.repositories.HospitalRepository;
import peaksoft.service.DoctorService;

import java.util.List;

/**
 * @author kurstan
 * @created at 18.02.2023 4:09
 */
@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final HospitalRepository hospitalRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, DepartmentRepository departmentRepository, AppointmentRepository appointmentRepository, HospitalRepository hospitalRepository) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.appointmentRepository = appointmentRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public List<Doctor> getAll(Long id) {
        return doctorRepository.getAllByHospitalId(id);
    }

    @Override
    public void save(Long hospitalId, Doctor doctor) {

        Hospital hospital = hospitalRepository
                .findById(hospitalId).orElseThrow(
                        () -> new NotFoundException("Hospital by id " + hospitalId + " is not found!"));
        for (Doctor d : doctorRepository.getAllByHospitalId(hospitalId)) {
            if (d.getEmail().equals(doctor.getEmail())) {
                throw new ExistsInDataBase("exists");
            }
        }
        doctor.setHospital(hospital);
        doctorRepository.save(doctor);
    }

    @Override
    public Doctor findById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(
                        ()-> new NotFoundException("Doctor by id " + doctorId + " not found"));
    }

    @Override
    public void update(Long doctorId, Doctor doctor) {
        Doctor oldDoctor = doctorRepository
                .findById(doctorId).orElseThrow(
                        () -> new NotFoundException("Doctor by id " + doctorId + " is not found!"));
        List<Doctor> allDoctors = doctorRepository.getAllByHospitalId(oldDoctor.getHospital().getId());
        for (Doctor d : allDoctors) {
            if (!d.getId().equals(doctorId) && d.getEmail().equals(doctor.getEmail())) {
                throw new ExistsInDataBase("exists");
            }
        }
        oldDoctor.setFirstName(doctor.getFirstName());
        oldDoctor.setLastName(doctor.getLastName());
        oldDoctor.setEmail(doctor.getEmail());
        oldDoctor.setPosition(doctor.getPosition());
    }

    @Override
    public void delete(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(
                        ()-> new NotFoundException("Doctor by id " + doctorId + " not found"));
        Hospital hospital = doctor.getHospital();
        List<Appointment> appointments = doctor.getAppointments();

        appointments.forEach(a-> a.getDoctor().setAppointments(null));
        appointments.forEach(a-> a.getPatient().setAppointments(null));

        appointments.forEach(a-> a.setDoctor(null));
        appointments.forEach(a-> a.setDepartment(null));
        appointments.forEach(a-> a.setPatient(null));
        hospital.getAppointments().removeAll(appointments);
        appointmentRepository.deleteAll(appointments);
        doctorRepository.deleteById(doctorId);
    }

    @Override
    public List<Department> getDepartments(Long doctorId) {
        return doctorRepository.getDoctorDepartments(doctorId);
    }

    @Override
    public void assignToDepartment(Long doctorId, Doctor doctor) {
        Department department = departmentRepository
                .findById(doctor.getDepartmentId()).orElseThrow(
                        ()-> new NotFoundException("Department not found"));
        Doctor oldDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(
                        ()-> new NotFoundException("Doctor by id " + doctorId + " not found"));
        oldDoctor.addDepartment(department);
    }

    @Override
    public void deleteDepartment(Long doctorId, Long departmentId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(
                        ()-> new NotFoundException("Doctor by id " + doctorId + " not found"));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()-> new NotFoundException("Department not found"));
        doctor.getDepartments().remove(department);
    }

    @Override
    public List<Appointment> getAppointments(Long doctorId) {
        return doctorRepository.getAppointments(doctorId);
    }

    @Override
    public List<Department> getCanBeAssignDepartments(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(
                        ()-> new NotFoundException("Doctor by id " + doctorId + " not found"));
        List<Department> allDep = departmentRepository.getAllByHospitalId(doctor.getHospital().getId());
        if (!doctor.getDepartments().isEmpty()) {
            allDep.removeAll(doctor.getDepartments());
        }
        return allDep;
    }
}
