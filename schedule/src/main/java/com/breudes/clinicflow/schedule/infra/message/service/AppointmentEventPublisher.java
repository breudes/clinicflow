package com.breudes.clinicflow.schedule.infra.message.service;

import com.breudes.clinicflow.schedule.entity.Appointment;
import com.breudes.clinicflow.schedule.infra.message.RabbitMQConfig;
import com.breudes.clinicflow.schedule.infra.message.dto.AppointmentEvent;
import com.breudes.clinicflow.schedule.infra.message.dto.EventType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppointmentEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public AppointmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEvent(Appointment appointment, EventType eventType) {
        AppointmentEvent event = new AppointmentEvent(
                appointment.getId(),
                eventType,
                appointment.getPatient().getEmail(),
                appointment.getDateTime(),
                appointment.getTimezone()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "",
                event
        );
    }
}