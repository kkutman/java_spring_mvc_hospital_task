package peaksoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.entity.Hospital;

import java.util.List;

/**
 * @author kurstan
 * @created at 28.02.2023 15:25
 */
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    @Query("from Hospital where name iLike (:keyWord)")
    List<Hospital> search(String keyWord);
}
