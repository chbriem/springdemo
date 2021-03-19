package com.example.springdemo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity http) throws Exception {
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // from https://blog.jdriven.com/2019/10/spring-security-5-2-oauth-2-exploration-part1/
            final List<String> authorities = (List<String>) jwt.getClaims().get("authorities");
            return (authorities).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .anyRequest().hasAuthority("ROLE_INTERFACES")
            .and() //
            .oauth2ResourceServer().jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter);
    }
}