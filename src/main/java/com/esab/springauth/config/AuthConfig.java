package com.esab.springauth.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.esab.springauth.config.auth.OurSecurityFilter;
import com.esab.springauth.config.auth.SecurityLoggingFilter;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class AuthConfig {
  @Autowired
  OurSecurityFilter securityFilter;

  @Autowired
  private AccessDeniedHandler customAccessDeniedHandler;

  @Autowired
  private AuthenticationEntryPoint customAuthenticationEntryPoint;

  private static final String[] SWAGGER_WHITELIST = {
      "/swagger-ui/**",
      "/v3/api-docs/**",
      "/swagger-resources/**",
      "/swagger-resources",
  };

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(SWAGGER_WHITELIST).permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/*").permitAll()
            .requestMatchers("/api/v1/authors").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/books").hasRole("USER")
            .anyRequest().authenticated())
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .accessDeniedHandler(customAccessDeniedHandler)
            .authenticationEntryPoint(customAuthenticationEntryPoint))
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(new SecurityLoggingFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean(name = "customGrantedAuthorityDefaults")
  static GrantedAuthorityDefaults grantedAuthorityDefaults() {

    return new GrantedAuthorityDefaults("ROLE_");
  }

  @Bean
  GroupedOpenApi publicApi() {
    return GroupedOpenApi
        .builder()
        .group("public-apis")
        .pathsToMatch("/**")
        .build();
  }

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("API title").version("API vesion"))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(
            new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
  }
}