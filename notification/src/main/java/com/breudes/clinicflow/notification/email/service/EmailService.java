package com.breudes.clinicflow.notification.email.service;

import com.breudes.clinicflow.notification.email.dto.AppointmentEvent;
import com.breudes.clinicflow.notification.email.exception.EmailException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailTemplateService emailTemplateService;
    @Value("${app.mail.from}")
    private final String from;

    public EmailService(JavaMailSender mailSender, EmailTemplateService emailTemplateService,@Value("${app.mail.from}") String from) {
        this.mailSender = mailSender;
        this.emailTemplateService = emailTemplateService;
        this.from = from;
    }

    private void sendEmail(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =  new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EmailException("Error while sending email: ", ex);
        }
    }

    public void sendCompleted(AppointmentEvent event){
        sendEmail(
                event.patientEmail(),
                "Medical appointment completed",
                emailTemplateService.completed(event)
        );
    }

    public void sendConfirmation(AppointmentEvent event) {
        sendEmail(
                event.patientEmail(),
                "Schedule confirmed for medical appointment",
                emailTemplateService.confirmation(event)
        );
    }

    public void sendUpdate(AppointmentEvent event) {
        sendEmail(
                event.patientEmail(),
                "Update in medical appointment",
                emailTemplateService.update(event)
        );
    }

    public void sendCancellation(AppointmentEvent event) {
        sendEmail(
                event.patientEmail(),
                "Cancellation of medical appointment",
                emailTemplateService.cancellation(event)
        );
    }
}