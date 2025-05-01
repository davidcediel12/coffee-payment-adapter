package com.cordilleracoffee.paymentadapter.config;


import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfig {


    @Bean
    public ReactiveCircuitBreaker paymentGatewayCircuitBreaker(ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        return circuitBreakerFactory.create("paymentGateway");
    }
}
