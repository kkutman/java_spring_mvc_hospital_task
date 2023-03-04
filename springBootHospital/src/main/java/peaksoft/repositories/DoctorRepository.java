package peaksoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.entity.Appointment;
import peaksoft.entity.Department;
import peaksoft.entity.Doctor;

import java.util.List;

/**
 * @author kurstan
 * @created at 28.02.2023 15:25
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("from Doctor d where d.hospital.id = :id")
    List<Doctor> getAllByHospitalId(Long id);

    @Query("select dep from Doctor doc join doc.departments dep where doc.id = :doctorId")
    List<Department> getDoctorDepartments(Long doctorId);

    @Query("select a from Appointment a join a.doctor d where d.id = :doctorId")
    List<Appointment> getAppointments(Long doctorId);
}
