package com.breudes.clinicflow.schedule.dto.appointment;

import java.time.LocalDateTime;

public record RescheduleAppointmentDTO(
        Long id,
        LocalDateTime dateTime
) {
}
