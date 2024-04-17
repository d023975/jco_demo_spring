package com.sap.jco_demo_spring.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@RequiredArgsConstructor
//@AllArgsConstructor
@Configuration
@ConfigurationProperties("vcap.services.xsuaa-papm-broker.credentials")
public class XsuaaProperties
{
    private String clientid;

}
