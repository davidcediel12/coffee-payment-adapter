package com.cordilleracoffee.paymentadapter.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "api.client")
@Component
@Getter
@Setter
public class ApiClientProperties {

    private String paymentGatewayUrl;
}
