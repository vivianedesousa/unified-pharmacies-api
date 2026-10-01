package com.farmacies.unifiedpharmacies.mailtrap;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

@Service
public class MailtrapService {
    private final MailtrapClient mailtrapClient;
    @Value("${mailtrap.from-email}")
    private String fromEmail;

    public MailtrapService(MailtrapClient mailtrapClient) {
        this.mailtrapClient = mailtrapClient;
    }

    public void sendEmail(
            String recipientEmail,
            String subject,
            String body) {

        MailtrapMail email =
                MailtrapMail.builder()
                        .from(new Address(fromEmail))
                        .to(List.of(new Address(recipientEmail)))
                        .subject(subject)
                        .text(body)
                        .build();

        try {
            mailtrapClient.send(email);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to send email through Mailtrap.",
                    e
            );
        }
    }
}

