package com.cordilleracoffee.paymentadapter.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


    @Bean
    public WebClient paymentGatewayClient(ApiClientProperties apiClientProperties) {

        return WebClient.builder()
                .baseUrl(apiClientProperties.getPaymentGatewayUrl())
                .build();
    }
}
