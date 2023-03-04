package peaksoft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author kurstan
 * @created at 01.03.2023 14:06
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){
        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();

        UserDetails admin = userBuilder
                .username("Admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        UserDetails doctor = userBuilder
                .username("Doctor")
                .password("doctor")
                .roles("DOCTOR")
                .build();

        UserDetails patient = userBuilder
                .username("Patient")
                .password("patient")
                .roles("PATIENT")
                .build();
        return new InMemoryUserDetailsManager(admin, doctor, patient);
    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/hospitals").permitAll()
                .requestMatchers("/hospitals/{id}").permitAll()
                .requestMatchers("/hospitals/new").hasRole("ADMIN")
                .requestMatchers("/hospitals/save").hasRole("ADMIN")
                .requestMatchers("/hospitals/{id}/delete").hasRole("ADMIN")
                .requestMatchers("/hospitals/{id}/edit").hasRole("ADMIN")
                .requestMatchers("/hospitals/{id}/update").hasRole("ADMIN")

                .requestMatchers("/{hospitalId}/doctors").permitAll()
                .requestMatchers("/{hospitalId}/doctors/new").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/doctors/save").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/doctors/edit").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/doctors/update").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/doctors/delete").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/doctors/{doctorId}/departments").permitAll()
                .requestMatchers("/{hospitalId}/doctors/{doctorId}/newDepartment").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/doctors/{doctorId}/saveDepartment").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/doctors/{doctorId}/{departmentId}/delete").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/doctors/{doctorId}/appointments").permitAll()

                .requestMatchers("/{hospitalId}/patients").permitAll()
                .requestMatchers("/{hospitalId}/patients/new").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/{hospitalId}/patients/save").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/{hospitalId}/patients/{patientId}/edit").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/{hospitalId}/patients/{patientId}/update").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/{hospitalId}/patients/{patientId}/delete").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/{hospitalId}/patients/{patientId}/appointments").permitAll()
                .requestMatchers("/{hospitalId}/patients/{patientId}/newApp").hasRole("PATIENT")
                .requestMatchers("/{hospitalId}/patients/{patientId}/saveApp").hasRole("PATIENT")
                .requestMatchers("/{hospitalId}/patients/{patientId}/{appointmentId}/delete").hasRole("PATIENT")

                .requestMatchers("/{hospitalId}/departments").permitAll()
                .requestMatchers("/{hospitalId}/departments/new").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/departments/save").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/departments/{departmentId}/edit").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/departments/{departmentId}/update").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/departments/{departmentId}/delete").hasRole("ADMIN")
                .requestMatchers("/{hospitalId}/departments/{departmentId}/doctors").hasRole("ADMIN")

                .requestMatchers("/{hospitalId}/appointments").permitAll()
                .requestMatchers("/{hospitalId}/appointments/new").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/{hospitalId}/appointments/save").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/{hospitalId}/appointments/{appointmentId}/edit").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/{hospitalId}/appointments/{appointmentId}/update").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers("/{hospitalId}/appointments/{appointmentId}/delete").hasAnyRole("ADMIN", "DOCTOR")
                .and()
                .formLogin()
                .defaultSuccessUrl("/hospitals")
                .permitAll();

                return http.build();
    }
}
