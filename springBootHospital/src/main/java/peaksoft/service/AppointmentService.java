package peaksoft.service;

import peaksoft.entity.Appointment;

import java.util.List;

/**
 * @author kurstan
 * @created at 18.02.2023 12:24
 */
public interface AppointmentService {
    List<Appointment> getAll(Long hospitalId);

    void save(Long hospitalId, Appointment appointment);

    Appointment findById(Long appointmentId);

    void update(Long appointmentId, Appointment appointment);

    void delete(Long id, Long appointmentId);

}
