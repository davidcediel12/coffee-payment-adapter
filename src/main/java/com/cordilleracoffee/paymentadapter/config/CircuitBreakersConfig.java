package com.cordilleracoffee.paymentadapter.config;


import com.cordilleracoffee.paymentadapter.application.exception.InvalidPaymentException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Duration;

@Configuration
public class CircuitBreakersConfig {


    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {

        return factory -> factory.configure(
                builder -> builder
                        .circuitBreakerConfig(CircuitBreakerConfig.custom()
                                .ignoreExceptions(InvalidPaymentException.class)
                                .ignoreException(InvalidPaymentException.class::isInstance)
                                .slidingWindowSize(10)
                                .minimumNumberOfCalls(5)
                                .failureRateThreshold(50)
                                .waitDurationInOpenState(Duration.ofSeconds(10))
                                .build()), "paymentGateway");
    }

    @Bean
    @DependsOn("defaultCustomizer")
    public ReactiveCircuitBreaker paymentGatewayCircuitBreaker(ReactiveCircuitBreakerFactory<?, ?>  factory) {
        return factory.create("paymentGateway");
    }
}
