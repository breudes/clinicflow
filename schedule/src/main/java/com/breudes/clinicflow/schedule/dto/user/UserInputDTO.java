package com.breudes.clinicflow.schedule.dto.user;

import com.breudes.clinicflow.schedule.entity.enums.UserType;

import java.time.LocalDate;

public record UserInputDTO(
        String name,
        String username,
        String password,
        String email,
        LocalDate birthDate,
        UserType userType,
        String license,
        String specialty,
        Boolean active
) {
}
