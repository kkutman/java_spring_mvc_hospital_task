package peaksoft.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import peaksoft.entity.Appointment;
import peaksoft.entity.Hospital;
import peaksoft.entity.Patient;
import peaksoft.exeptions.NotFoundException;
import peaksoft.repositories.AppointmentRepository;
import peaksoft.repositories.HospitalRepository;
import peaksoft.repositories.PatientRepository;
import peaksoft.service.PatientService;

import java.util.List;
import java.util.Optional;

/**
 * @author kurstan
 * @created at 17.02.2023 22:54
 */
@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final HospitalRepository hospitalRepository;

    public PatientServiceImpl(PatientRepository patientRepository, AppointmentRepository appointmentRepository, HospitalRepository hospitalRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public List<Patient> getAllByHospitalId(Long patientId) {
        return patientRepository.getAllByHospitalId(patientId);
    }

    @Override
    public void save(Long hospitalId, Patient patient) {
        Hospital hospital = hospitalRepository
                .findById(hospitalId).orElseThrow(
                        () -> new NotFoundException("Hospital by id " + hospitalId + " is not found!"));
        patient.setHospital(hospital);
        patientRepository.save(patient);
    }

    @Override
    public Patient getById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(
                        ()-> new NotFoundException("Patient by id " + patientId + " not found"));
    }

    @Override
    public void update(Long patientId, Patient patient) {
        Patient oldPatient = patientRepository
                .findById(patientId).orElseThrow(
                        () -> new NotFoundException("Patient by id " + patientId + " is not found!"));
        oldPatient.setFirstName(patient.getFirstName());
        oldPatient.setLastName(patient.getLastName());
        oldPatient.setPhoneNumber(patient.getPhoneNumber());
        oldPatient.setEmail(patient.getEmail());
        oldPatient.setGender(patient.getGender());
        patientRepository.save(oldPatient);
    }

    @Override
    public void delete(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(
                        ()-> new NotFoundException("Patient by id " + patientId + " not found"));
        Hospital hospital = patient.getHospital();
        List<Appointment> appointments = patient.getAppointments();

        appointments.forEach(a-> a.getPatient().setAppointments(null));
        appointments.forEach(a-> a.getDoctor().setAppointments(null));

        appointments.forEach(a-> a.setPatient(null));
        appointments.forEach(a-> a.setDoctor(null));
        appointments.forEach(a-> a.setDepartment(null));
        hospital.getAppointments().removeAll(appointments);
        appointmentRepository.deleteAll(appointments);
        patientRepository.deleteById(patientId);
//        patientRepository.deleteById(patientId);
    }

    @Override
    public List<Appointment> getAppointments(Long id, Long patientId) {
        return appointmentRepository
                .getAllByHospitalId(id).stream().filter(a-> a.getPatient().getId()
                        .equals(patientId)).toList();
    }
}
