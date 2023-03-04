package peaksoft.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.enums.Gender;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

/**
 * @author kurstan
 * @created at 17.02.2023 12:06
 */
@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "patient_id_gen"
    )
    @SequenceGenerator(
            name = "patient_id_gen",
            sequenceName = "patient_seq",
            allocationSize = 1
    )
    private Long id;
    @NotEmpty(message = "First name should not be empty!")
    @Size(min = 2, max = 33, message = "First name should be between 2 and 33 characters!")
    @Column(name = "first_name")
    private String firstName;
    @NotEmpty(message = "Last name should not be empty!")
    @Size(min = 2, max = 33, message = "Last name should be between 2 and 33 characters!")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    @NotEmpty(message = "Phone number should not be empty!")
    @Pattern(regexp = "\\+996\\d{9}", message = "Phone number should start with +996 and consist of 13 characters!")
    private String phoneNumber;
    @NotNull(message = "Choice gender!")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @NotEmpty(message = "Email should not be empty!")
    @Email(message = "Please provide a valid email address!")
    @Column(name = "email", unique = true)
    private String email;

    @ManyToOne(cascade = {REFRESH, DETACH, MERGE, PERSIST})
    private Hospital hospital;

    @OneToMany(mappedBy = "patient", cascade = ALL, fetch = FetchType.EAGER)
    private List<Appointment> appointments;
    public void addAppointment(Appointment appointment){
        if (appointments == null){
            appointments = new ArrayList<>();
        }
        appointments.add(appointment);
    }
}
