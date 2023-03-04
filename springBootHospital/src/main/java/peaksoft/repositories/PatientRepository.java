package peaksoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.entity.Patient;

import java.util.List;

/**
 * @author kurstan
 * @created at 28.02.2023 15:26
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("from Patient p join p.hospital h where h.id = :hospitalId")
    List<Patient> getAllByHospitalId(Long hospitalId);
}
