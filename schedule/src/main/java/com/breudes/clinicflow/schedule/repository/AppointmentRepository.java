package com.breudes.clinicflow.schedule.repository;

import com.breudes.clinicflow.schedule.entity.Appointment;
import com.breudes.clinicflow.schedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(User patient);
    List<Appointment> findByDoctor(User doctor);
}