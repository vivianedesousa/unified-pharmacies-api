package com.farmacies.unifiedpharmacies.config;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailtrapClientConfig {
    @Bean
    public MailtrapClient mailtrapClient(
            @Value("${mailtrap.token}") String token,
            @Value("${mailtrap.inbox-id}") long inboxId) {
        //@value esta dizendo spring me da o valor dessa conf aqui
        final MailtrapConfig config = new MailtrapConfig.Builder()
                .sandbox(true)
                .inboxId(inboxId)
                .token(token)
                .build();

        return MailtrapClientFactory.createMailtrapClient(config);
    }
}



