package com.nirvana.springreactorexample.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ProfileWebClientFactory {

    @Value("${nirvana.services.profile.baseUrl}")
    private String baseUrl;

    @Bean
    public WebClient profileWebClient() {
        return WebClient.builder().baseUrl(baseUrl).build();
    }
}
