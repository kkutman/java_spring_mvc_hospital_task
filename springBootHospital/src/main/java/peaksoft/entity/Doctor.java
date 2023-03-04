package peaksoft.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "doctor_id_gen"
    )
    @SequenceGenerator(
            name = "doctor_id_gen",
            sequenceName = "doctor_seq",
            allocationSize = 1
    )
    private Long id;
    @NotEmpty(message = "First name should not be empty!")
    @Size(min = 2, max = 33, message = "First name should be between 2 and 33 characters!")
    private String firstName;
    @NotEmpty(message = "Last name should not be empty!")
    @Size(min = 2, max = 33, message = "Last name should be between 2 and 33 characters!")
    private String lastName;
    @NotEmpty(message = "Position should not be empty!")
    private String position;
    @NotEmpty(message = "Email should not be empty!")
    @Email(message = "Please provide a valid email address!")
    @Column(name = "email", unique = true)
    private String email;
    @ManyToOne(cascade = {REFRESH, DETACH, MERGE, PERSIST})
    private Hospital hospital;
    @ManyToMany(cascade = {REFRESH, DETACH, MERGE, PERSIST})
    private List<Department> departments;
    public void addDepartment(Department department){
        if (departments == null){
            departments = new ArrayList<>();
        }
        departments.add(department);
    }
    @OneToMany(mappedBy = "doctor", cascade = ALL, fetch = FetchType.EAGER)
    private List<Appointment> appointments;
    public void addAppointment(Appointment appointment){
        if (appointments == null){
            appointments = new ArrayList<>();
        }
        appointments.add(appointment);
    }
    @Transient
    private Long departmentId;
}
