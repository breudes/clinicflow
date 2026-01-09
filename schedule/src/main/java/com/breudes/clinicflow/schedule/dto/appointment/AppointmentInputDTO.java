package com.breudes.clinicflow.schedule.dto.appointment;

import com.breudes.clinicflow.schedule.entity.User;
import com.breudes.clinicflow.schedule.entity.enums.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentInputDTO(
        User patient,
        User doctor,
        AppointmentStatus status,
        LocalDateTime dateTime,
        String timezone
) {
}
