package peaksoft.service;

import peaksoft.entity.Appointment;
import peaksoft.entity.Patient;

import java.util.List;

/**
 * @author kurstan
 * @created at 17.02.2023 22:52
 */
public interface PatientService {
    List<Patient> getAllByHospitalId(Long patientId);
    void save(Long hospitalId, Patient patient);

    Patient getById(Long patientId);

    void update(Long patientId, Patient patient);

    void delete(Long patientId);

    List<Appointment> getAppointments(Long id, Long patientId);
}
