package com.breudes.clinicflow.schedule.dto.appointment;

import com.breudes.clinicflow.schedule.entity.Appointment;
import com.breudes.clinicflow.schedule.entity.User;
import com.breudes.clinicflow.schedule.entity.enums.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentOutputDTO(
        Long id,
        User patient,
        User doctor,
        AppointmentStatus status,
        LocalDateTime dateTime,
        String timezone
) {
    public static AppointmentOutputDTO fromEntity(Appointment appointment) {
        return new AppointmentOutputDTO(
                appointment.getId(),
                appointment.getPatient(),
                appointment.getDoctor(),
                appointment.getStatus(),
                appointment.getDateTime(),
                appointment.getTimezone()
        );
    }

    public String toFormattedString() {
        return "\n Appointment ID: " + id + "\n" +
            "Patient: " + patient.getName() + " (" + patient.getEmail() + ")\n" +
            "Doctor: " + doctor.getName() + " - " + doctor.getSpecialty() + "\n" +
            "Status: " + status + "\n" +
            "Date/Time: " + dateTime + "\n" +
            "Timezone: " + (timezone != null ? timezone : "not informed") + "\n";
    }
}
