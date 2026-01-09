package com.breudes.clinicflow.notification.email;

import com.breudes.clinicflow.notification.config.RabbitMQConfig;
import com.breudes.clinicflow.notification.email.dto.AppointmentEvent;
import com.breudes.clinicflow.notification.email.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventListener {
    @Autowired
    private final EmailService emailService;

    public AppointmentEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consumeEvent(AppointmentEvent event) {
        try {
            System.out.println("📧 Sending email to: " + event.patientEmail());
            System.out.println("Appointment in:  " + event.appointmentDate());
            System.out.println("EventType:  " + event.eventType());

            switch (event.eventType()) {
                case APPOINTMENT_COMPLETED ->
                        emailService.sendCompleted(event);
                case APPOINTMENT_SCHEDULED ->
                        emailService.sendConfirmation(event);
                case APPOINTMENT_UPDATED ->
                        emailService.sendUpdate(event);
                case APPOINTMENT_CANCELLED ->
                        emailService.sendCancellation(event);
            }
        } catch (Exception ex) {
            System.out.println("Erro ao enviar e-mail para evento " + event + " - " + ex.getMessage());
        }
    }
}