package peaksoft.service;

import peaksoft.entity.Department;
import peaksoft.entity.Doctor;

import java.util.List;

/**
 * @author kurstan
 * @created at 18.02.2023 11:04
 */
public interface DepartmentService {
    List<Department> getAll(Long id);

    void update(Long departmentId, Department department);

    void save(Long hospitalId, Department department);

    void delete(Long departmentId);

    Department findById(Long departmentId);

    List<Doctor> getDoctors(Long departmentId);
}
