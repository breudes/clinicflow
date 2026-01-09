package com.breudes.clinicflow.schedule.entity;

import com.breudes.clinicflow.schedule.dto.appointment.AppointmentInputDTO;
import com.breudes.clinicflow.schedule.entity.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "patient_id")
    private User patient;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "doctor_id")
    private User doctor;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;
    private String timezone;

    public Appointment() {
    }

    public Appointment(Long id, User patient, User doctor, AppointmentStatus status, LocalDateTime dateTime, String timezone) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.status = status;
        this.dateTime = dateTime;
        this.timezone = timezone;
    }

    public Appointment(AppointmentInputDTO appointmentInputDTO){
        this.patient = appointmentInputDTO.patient();
        this.doctor = appointmentInputDTO.doctor();
        this.status = AppointmentStatus.SCHEDULED;
        this.dateTime = appointmentInputDTO.dateTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", status=" + status +
                ", date=" + dateTime +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}
