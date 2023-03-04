package peaksoft.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import peaksoft.entity.Appointment;
import peaksoft.entity.Department;
import peaksoft.entity.Hospital;
import peaksoft.exeptions.BadRequestException;
import peaksoft.exeptions.NotFoundException;
import peaksoft.repositories.AppointmentRepository;
import peaksoft.repositories.*;
import peaksoft.service.AppointmentService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kurstan
 * @created at 18.02.2023 12:25
 */
@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, HospitalRepository hospitalRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, DepartmentRepository departmentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Appointment> getAll(Long hospitalId) {
        List<Appointment> appointments = new ArrayList<>();
        int size = appointmentRepository.getAllByHospitalId(hospitalId).size();
        for (int i = 0; i < size; i++) {
            appointments.add(appointmentRepository.getAllByHospitalId(hospitalId).get(size - 1 - i));
        }
        return appointments;
    }
    @Override
    public void save(Long hospitalId, Appointment appointment) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(
                        ()-> new NotFoundException("Hospital by id " + hospitalId + " not found"));
        appointment.setDoctor(doctorRepository
                .findById(appointment.getDoctorId()).orElseThrow(
                        ()-> new NotFoundException("Doctor not found")));
        List<Department> departments = appointment.getDoctor().getDepartments();
        for (Department department : departments) {
            if (!department.getId().equals(appointment.getDepartmentId())){
                throw new BadRequestException("department error");
            }
        }
        appointment.setDepartment(departmentRepository
                .findById(appointment.getDepartmentId()).orElseThrow(
                        ()-> new NotFoundException("Department not found")));
        appointment.setPatient(patientRepository
                .findById(appointment.getPatientId()).orElseThrow(
                        ()-> new NotFoundException("Patient not found")));
        hospital.addAppointment(appointment);
        appointmentRepository.save(appointment);
    }

    @Override
    public Appointment findById(Long appointmentId) {
        return appointmentRepository
                .findById(appointmentId).orElseThrow(
                        ()-> new NotFoundException("Appointment by id " + appointmentId + " not found"));
    }

    @Override
    public void update(Long appointmentId, Appointment appointment) {
        Appointment oldAppointment = appointmentRepository
                .findById(appointmentId).orElseThrow(
                        () -> new NotFoundException("Appointment by id " + appointmentId + " is not found!"));
        oldAppointment.setDate(appointment.getDate());
        oldAppointment.setDoctor(doctorRepository.findById(appointment.getDoctorId())
                .orElseThrow(()-> new NotFoundException("Doctor not found")));
        List<Department> departments = oldAppointment.getDoctor().getDepartments();
        for (Department department : departments) {
            if (!department.getId().equals(appointment.getDepartmentId())){
                throw new BadRequestException("department error");
            }
        }
        oldAppointment.setPatient(patientRepository.findById(appointment.getPatientId())
                .orElseThrow(()-> new NotFoundException("Patient not found")));
        oldAppointment.setDepartment(departmentRepository.findById(appointment.getDepartmentId())
                .orElseThrow(()-> new NotFoundException("Department not found")));

    }

    @Override
    public void delete(Long id, Long appointmentId) {
        Appointment appointment = appointmentRepository
                .findById(appointmentId).orElseThrow(
                        ()-> new NotFoundException("Appointment by id " + appointmentId + " not found"));

        hospitalRepository.findById(id)
                .orElseThrow(
                        ()-> new NotFoundException("Hospital by id " + id + " not found"))
                        .getAppointments().remove(appointment);
        appointment.getDoctor().getAppointments().clear();
        appointment.getPatient().getAppointments().clear();
        appointmentRepository.delete(appointment);
    }
}
