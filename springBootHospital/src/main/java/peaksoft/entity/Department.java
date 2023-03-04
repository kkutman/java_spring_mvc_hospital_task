package peaksoft.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

/**
 * @author kurstan
 * @created at 17.02.2023 12:06
 */
@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "department_id_gen"
    )
    @SequenceGenerator(
            name = "department_id_gen",
            sequenceName = "department_seq",
            allocationSize = 1
    )
    private Long id;
    @NotEmpty(message = "Department name should not be empty!")
    @Size(min = 2, max = 33, message = "Department name should be between 2 and 33 characters!")
    @Column(name = "name", unique = true)
    private String name;
    @ManyToOne(cascade = {REFRESH, DETACH, MERGE, PERSIST})
    private Hospital hospital;
    @ManyToMany(mappedBy = "departments", cascade = {REFRESH, DETACH, MERGE, PERSIST}, fetch = FetchType.EAGER)
    private List<Doctor> doctors;
    public void addDoctor(Doctor doctor){
        if (doctors == null){
            doctors = new ArrayList<>();
        }
        doctors.add(doctor);
    }
}
