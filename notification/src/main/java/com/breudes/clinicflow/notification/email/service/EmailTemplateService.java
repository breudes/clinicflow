package com.breudes.clinicflow.notification.email.service;

import com.breudes.clinicflow.notification.email.dto.AppointmentEvent;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailTemplateService {
    private final TemplateEngine templateEngine;

    public EmailTemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String completed(AppointmentEvent event){
        return process("email/completed", event);
    }

    public String confirmation(AppointmentEvent event) {
        return process("email/confirmation", event);
    }

    public String update(AppointmentEvent event) {
        return process("email/update", event);
    }

    public String cancellation(AppointmentEvent event) {
        return process("email/cancellation", event);
    }

    private String process(String template, AppointmentEvent event) {
        Context context = new Context();
        context.setVariable("appointmentId", event.appointmentId());
        context.setVariable("appointmentDate", event.appointmentDate()
             .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        context.setVariable("doctorEmail", event.doctorEmail());
        return templateEngine.process(template, context);
    }
}