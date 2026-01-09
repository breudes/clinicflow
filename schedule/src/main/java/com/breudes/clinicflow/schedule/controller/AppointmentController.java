package com.breudes.clinicflow.schedule.controller;

import com.breudes.clinicflow.schedule.dto.appointment.AppointmentInputDTO;
import com.breudes.clinicflow.schedule.dto.appointment.AppointmentOutputDTO;
import com.breudes.clinicflow.schedule.dto.appointment.RescheduleAppointmentDTO;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id){
        return appointmentService.cancelAppointment(id);
    }

    @PatchMapping("/complete/{appointmentId}")
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

    @GetMapping("/list-by-doctor/{doctorId}")
    public ResponseEntity<String> listAllByDoctor(@PathVariable Long doctorId){
        List<AppointmentOutputDTO> appointments = appointmentService.listAppointmentByDoctor(doctorId);
        if(appointments.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No appointments found for the given doctor ID.");
        } else {
            StringBuilder formattedAppointments = new StringBuilder("List of appointments by doctor:\n");
            appointments.forEach(a -> formattedAppointments.append(a.toFormattedString()));
            return ResponseEntity.ok(formattedAppointments.toString());
        }
    }

    @GetMapping("/list-by-patient/{patientId}")
    public ResponseEntity<String> listAllByPatient(@PathVariable Long patientId){
        List<AppointmentOutputDTO> appointments = appointmentService.listAppointmentByPatient(patientId);
        if(appointments.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No appointments found for the given patient ID.");
        } else {
            StringBuilder formattedAppointments = new StringBuilder("List of appointments by patient:\n");
            appointments.forEach(a -> formattedAppointments.append(a.toFormattedString()));
            return ResponseEntity.ok(formattedAppointments.toString());
        }
    }
}