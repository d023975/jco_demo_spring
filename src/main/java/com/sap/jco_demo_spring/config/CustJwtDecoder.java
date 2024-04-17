package com.sap.jco_demo_spring.config;

import com.sap.cloud.security.spring.config.XsuaaServiceConfiguration;
import com.sap.cloud.security.spring.token.authentication.JwtDecoderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

public class CustJwtDecoder implements JwtDecoder {

    private final JwtDecoder defaultDecoder;

    public CustJwtDecoder(XsuaaServiceConfiguration xsuaaServiceConfiguration){
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-CustJwtDecoder");
        JwtDecoderBuilder jwtDecoderBuilder = new JwtDecoderBuilder();
        jwtDecoderBuilder.withXsuaaServiceConfiguration(xsuaaServiceConfiguration);
        this.defaultDecoder = jwtDecoderBuilder.build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-decode");
            return this.defaultDecoder.decode(token);
        } catch (Exception e) {
            throw new BadJwtException(e.getMessage());
        }

    }
}
