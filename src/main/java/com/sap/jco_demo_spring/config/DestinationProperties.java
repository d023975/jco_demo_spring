package com.sap.jco_demo_spring.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@RequiredArgsConstructor
//@AllArgsConstructor
@Configuration
@ConfigurationProperties("vcap.services.destination-papm-lite.credentials")
public class DestinationProperties {
    private String clientid;
    private String clientsecret;
    private String tenantid;
    private String url;
    private String tenantmode;
    private String uaadomain;
}
