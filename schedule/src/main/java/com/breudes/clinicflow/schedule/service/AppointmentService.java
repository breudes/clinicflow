package com.breudes.clinicflow.schedule.service;

import com.breudes.clinicflow.schedule.dto.appointment.AppointmentInputDTO;
import com.breudes.clinicflow.schedule.dto.appointment.AppointmentOutputDTO;
import com.breudes.clinicflow.schedule.dto.appointment.RescheduleAppointmentDTO;
import com.breudes.clinicflow.schedule.entity.Appointment;
import com.breudes.clinicflow.schedule.entity.User;
import com.breudes.clinicflow.schedule.entity.enums.AppointmentStatus;
import com.breudes.clinicflow.schedule.entity.enums.UserType;
import com.breudes.clinicflow.schedule.infra.message.dto.EventType;
import com.breudes.clinicflow.schedule.infra.message.service.AppointmentEventPublisher;
import com.breudes.clinicflow.schedule.repository.AppointmentRepository;
import com.breudes.clinicflow.schedule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.breudes.clinicflow.schedule.service.util.AuthenticatedUserData.getAuthenticatedUserId;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentEventPublisher appointmentEventPublisher;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public ResponseEntity<String> createAppointment(AppointmentInputDTO appointmentInputDTO) {
        if(appointmentInputDTO.patient() == null){
            return ResponseEntity.status(400).body("Patient data is required.");
        }
        if(appointmentInputDTO.doctor() == null){
            return ResponseEntity.status(400).body("Doctor data is required.");
        }
        if(appointmentInputDTO.dateTime().isEqual(LocalDateTime.now()) || appointmentInputDTO.dateTime().isBefore(LocalDateTime.now())){
            return ResponseEntity.status(400).body("Date must be after this actual local date.");
        }
        if(Objects.equals(appointmentInputDTO.patient().getId(), appointmentInputDTO.doctor().getId())){
            return ResponseEntity.status(400).body("Patient and doctor must have different IDs.");
        }

        Optional<User> foundPatient = userRepository.findById(appointmentInputDTO.patient().getId());
        Optional<User> foundDoctor = userRepository.findById(appointmentInputDTO.doctor().getId());

        if(foundPatient.isEmpty()){
            return ResponseEntity.status(400).body("Patient not found.");
        }
        if(foundDoctor.isEmpty()){
            return ResponseEntity.status(400).body("Doctor not found.");
        }

        if(foundDoctor.get().getUserType().equals(UserType.PATIENT) || foundDoctor.get().getUserType().equals(UserType.NURSE)){
            return ResponseEntity.status(401).body("Id isn't for a doctor user type.");
        }

        Appointment newAppointment = new Appointment(appointmentInputDTO);
        newAppointment.setDoctor(foundDoctor.get());
        newAppointment.setPatient(foundPatient.get());
        newAppointment.setStatus(AppointmentStatus.SCHEDULED);
        try{
            Appointment savedAppointment = appointmentRepository.save(newAppointment);

            appointmentEventPublisher.publishEvent(savedAppointment, EventType.APPOINTMENT_SCHEDULED);

            return ResponseEntity.status(201).body("Appointment created successfully.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> cancelAppointment(Long id) {
        Optional<Appointment> foundOptAppointment = appointmentRepository.findById(id);
        if (foundOptAppointment.isPresent()) {
            Appointment foundAppointment = foundOptAppointment.get();
            foundAppointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(foundAppointment);

            appointmentEventPublisher.publishEvent(foundAppointment, EventType.APPOINTMENT_CANCELLED);

            return ResponseEntity.status(200).body("Appointment successfully cancelled.");
        } else {
            return ResponseEntity.status(404).body("Appointment not found.");
        }
    }

    public ResponseEntity<String> completeAppointment(Long appointmentId){
        Optional<Appointment> foundOptAppointment = appointmentRepository.findById(appointmentId);
        if (foundOptAppointment.isPresent()) {
            Appointment foundAppointment = foundOptAppointment.get();
            foundAppointment.setStatus(AppointmentStatus.COMPLETED);
            appointmentRepository.save(foundAppointment);

            appointmentEventPublisher.publishEvent(foundAppointment, EventType.APPOINTMENT_COMPLETED);

            return ResponseEntity.status(200).body("Appointment successfully completed.");
        } else {
            return ResponseEntity.status(404).body("Appointment not found.");
        }
    }

    public ResponseEntity<String> rescheduleAppointment(RescheduleAppointmentDTO rescheduleAppointmentDTO) {
        Optional<Appointment> foundOptAppointment = appointmentRepository.findById(rescheduleAppointmentDTO.id());
        if (foundOptAppointment.isPresent()) {
            Appointment foundAppointment = foundOptAppointment.get();
            foundAppointment.setDateTime(rescheduleAppointmentDTO.dateTime());
            foundAppointment.setStatus(AppointmentStatus.SCHEDULED);
            appointmentRepository.save(foundAppointment);

            appointmentEventPublisher.publishEvent(foundAppointment, EventType.APPOINTMENT_UPDATED);

            return ResponseEntity.status(200).body("Appointment successfully rescheduled.");
        } else {
            return ResponseEntity.status(404).body("Appointment not found.");
        }
    }

    public ResponseEntity<String> updateAppointment(Long id, AppointmentOutputDTO appointmentOutputDTO) {
        Optional<Appointment> foundOptAppointment = appointmentRepository.findById(id);
        if (foundOptAppointment.isPresent()) {
            Appointment foundAppointment = foundOptAppointment.get();

            if(appointmentOutputDTO.patient() == null){
                return ResponseEntity.status(400).body("Patient data is required.");
            }
            if(appointmentOutputDTO.doctor() == null){
                return ResponseEntity.status(400).body("Doctor data is required.");
            }
            if(appointmentOutputDTO.dateTime().isEqual(LocalDateTime.now()) ||  appointmentOutputDTO.dateTime().isBefore(LocalDateTime.now())){
                return ResponseEntity.status(400).body("Date must be after this actual local date.");
            }
            if(Objects.equals(appointmentOutputDTO.patient().getId(), appointmentOutputDTO.doctor().getId())){
                return ResponseEntity.status(400).body("Patient and doctor must have different IDs.");
            }

            Optional<User> foundPatient = userRepository.findById(appointmentOutputDTO.patient().getId());
            Optional<User> foundDoctor = userRepository.findById(appointmentOutputDTO.doctor().getId());
            foundPatient.ifPresent(foundAppointment::setPatient);
            foundDoctor.ifPresent(foundAppointment::setDoctor);

            foundAppointment.setDateTime(appointmentOutputDTO.dateTime());
            foundAppointment.setStatus(AppointmentStatus.SCHEDULED);

            appointmentRepository.save(foundAppointment);

            appointmentEventPublisher.publishEvent(foundAppointment, EventType.APPOINTMENT_UPDATED);

            return ResponseEntity.status(200).body("Appointment successfully updated.");
        } else {
            return ResponseEntity.status(404).body("Appointment not found.");
        }
    }

    public List<AppointmentOutputDTO> listAppointmentByPatient(Long patientId) {
        // If a patient wants to see one or multiple appointments
        // Related to them
        User loggedUser = getAuthenticatedUserId();
        assert loggedUser != null;
        if(loggedUser.getUserType() == UserType.PATIENT){
            Optional<User> foundPatient = userRepository.findById(patientId);
            if(Objects.equals(loggedUser.getId(), patientId)){
                if(foundPatient.isPresent()){
                    List<Appointment> appointments = appointmentRepository.findByPatient(foundPatient.get());
                    return appointments.stream()
                            .map(AppointmentOutputDTO::fromEntity)
                            .collect(Collectors.toList());
            }
        } else {
                // Other cases
                if(foundPatient.isPresent()){
                    List<Appointment> appointments = appointmentRepository.findByPatient(foundPatient.get());
                    return appointments.stream()
                            .map(AppointmentOutputDTO::fromEntity)
                            .collect(Collectors.toList());
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public List<AppointmentOutputDTO> listAppointmentByDoctor(Long doctorId) {
        Optional<User> foundDoctor = userRepository.findById(doctorId);
        if(foundDoctor.isPresent()){
            List<Appointment> appointments = appointmentRepository.findByPatient(foundDoctor.get());
            return appointments.stream()
                    .map(AppointmentOutputDTO::fromEntity)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}