package peaksoft.service;

import peaksoft.entity.Appointment;
import peaksoft.entity.Department;
import peaksoft.entity.Doctor;

import java.util.List;

/**
 * @author kurstan
 * @created at 18.02.2023 4:09
 */
public interface DoctorService {
    List<Doctor> getAll(Long id);

    void save(Long hospitalId, Doctor doctor);

    Doctor findById(Long doctorId);

    void update(Long doctorId, Doctor doctor);

    void delete(Long doctorId);

    List<Department> getDepartments(Long doctorId);

    void assignToDepartment(Long doctorId, Doctor doctor);

    void deleteDepartment(Long doctorId, Long departmentId);

    List<Appointment> getAppointments(Long doctorId);
    List<Department> getCanBeAssignDepartments(Long doctorId);

}
