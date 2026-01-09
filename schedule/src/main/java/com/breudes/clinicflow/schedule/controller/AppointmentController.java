package com.breudes.clinicflow.schedule.controller;

import com.breudes.clinicflow.schedule.dto.appointment.AppointmentInputDTO;
import com.breudes.clinicflow.schedule.dto.appointment.AppointmentOutputDTO;
import com.breudes.clinicflow.schedule.dto.appointment.RescheduleAppointmentDTO;
import com.breudes.clinicflow.schedule.repository.AppointmentRepository;
import com.breudes.clinicflow.schedule.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<String> createAppointment(@RequestBody AppointmentInputDTO appointmentInputDTO) {
        try{
            return appointmentService.createAppointment(appointmentInputDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error while creating the appointment.");
        }
    }

    @GetMapping
    public ResponseEntity<String> listAll(){
        return ResponseEntity.status(HttpStatus.FOUND).body(
                appointmentRepository.findAll()
                        .stream()
                        .map(AppointmentOutputDTO::fromEntity)
                        .toList().toString()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id){
        return appointmentService.cancelAppointment(id);
    }

    @PatchMapping("/complete")
    public ResponseEntity<String> completeAppointment(@PathVariable Long appointmentId) {
        return appointmentService.completeAppointment(appointmentId);
    }

    @PatchMapping("/reschedule")
    public ResponseEntity<String> rescheduleAppointment(@RequestBody RescheduleAppointmentDTO rescheduleAppointmentDTO) {
        return appointmentService.rescheduleAppointment(rescheduleAppointmentDTO);
    }

    @PutMapping
    public ResponseEntity<String> updateAppointment(@RequestBody AppointmentOutputDTO appointmentOutputDTO){
        return appointmentService.updateAppointment(appointmentOutputDTO.id(), appointmentOutputDTO);
    }

    @GetMapping("/list-by-doctor")
    public List<AppointmentOutputDTO> listAllByDoctor(@RequestBody Long doctorId){
        return appointmentService.listAppointmentByDoctor(doctorId);
    }

    @GetMapping("/list-by-patient")
    public List<AppointmentOutputDTO> listAllByPatient(@RequestBody Long patientId){
        return appointmentService.listAppointmentByPatient(patientId);
    }
}