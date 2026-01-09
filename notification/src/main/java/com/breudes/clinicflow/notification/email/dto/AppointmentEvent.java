package com.breudes.clinicflow.notification.email.dto;

import com.breudes.clinicflow.notification.email.enums.EventType;
import java.time.LocalDateTime;

public record AppointmentEvent(
        Long appointmentId,
        EventType eventType,
        String patientEmail,
        String doctorEmail,
        LocalDateTime appointmentDate
) {
}
