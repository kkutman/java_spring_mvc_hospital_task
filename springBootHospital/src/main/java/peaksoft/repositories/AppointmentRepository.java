package peaksoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.entity.Appointment;

import java.util.List;

/**
 * @author kurstan
 * @created at 28.02.2023 15:22
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("select a from Hospital h join h.appointments a where h.id = :hospitalId")
    List<Appointment> getAllByHospitalId(Long hospitalId);
}
