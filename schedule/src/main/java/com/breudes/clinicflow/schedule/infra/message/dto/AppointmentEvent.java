package com.breudes.clinicflow.schedule.infra.message.dto;

import java.time.LocalDateTime;

public record AppointmentEvent(
        Long appointmentId,
        EventType eventType,
        String patientEmail,
        LocalDateTime appointmentDate,
        String timezone
) {
}
