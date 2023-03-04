package peaksoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.entity.Department;
import peaksoft.entity.Doctor;

import java.util.List;

/**
 * @author kurstan
 * @created at 28.02.2023 15:24
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("from Department d join d.hospital h where h.id = :hospitalId")
    List<Department> getAllByHospitalId(Long hospitalId);

    @Query("from Doctor doc join doc.departments dep where dep.id = :departmentId")
    List<Doctor> getDoctors(Long departmentId);
}
