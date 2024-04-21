package com.sap.jco_demo_spring.config;


import com.sap.cloud.security.spring.config.IdentityServicesPropertySourceFactory;
import com.sap.cloud.security.spring.config.XsuaaServiceConfiguration;
import com.sap.cloud.security.spring.token.authentication.AuthenticationToken;
import com.sap.cloud.security.token.TokenClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@PropertySource(factory = IdentityServicesPropertySourceFactory.class, ignoreResourceNotFound = true, value = { "" })
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    @Autowired
    XsuaaServiceConfiguration xsuaaServiceConfiguration;
    @Autowired
    Converter<Jwt, AbstractAuthenticationToken> authConverter;
    @Bean
    SecurityFilterChain customJwtSecurityChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers(antMatcher(GET,"/welcome")).hasAuthority("run_rfc")
                                .requestMatchers(antMatcher(GET,"/actuator/**")).permitAll()
                                .requestMatchers("/*").authenticated()
                                .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(new MyCustomHybridTokenAuthenticationConverter())
                        )
                );
        ; //.authenticationManager();
        return http.build();
    }

    class MyCustomHybridTokenAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        public AbstractAuthenticationToken convert(Jwt jwt) {
            if (jwt.hasClaim(TokenClaims.XSUAA.EXTERNAL_ATTRIBUTE)) {
                return authConverter.convert(jwt);
            }
            return new AuthenticationToken(jwt, deriveAuthoritiesFromGroup(jwt));
        }

        private Collection<GrantedAuthority> deriveAuthoritiesFromGroup(Jwt jwt) {
            Collection<GrantedAuthority> groupAuthorities = new ArrayList<>();
            if (jwt.hasClaim(TokenClaims.GROUPS)) {
                List<String> groups = jwt.getClaimAsStringList(TokenClaims.GROUPS);
                for (String group : groups) {
                    groupAuthorities.add(new SimpleGrantedAuthority(group.replace("IASAUTHZ_", "")));
                }
            }
            return groupAuthorities;
        }
    }

    private JwtDecoder jwtDecoder() {
        return new CustJwtDecoder(xsuaaServiceConfiguration);
    }
}
