package com.example.exercicio_springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() //depois de ter um primeiro usuário, apenas ADM pode registrar
                        .requestMatchers(HttpMethod.POST, "/contatos", "/contatos/*").hasRole("ADMIN") // rota protegida
                        .requestMatchers(HttpMethod.POST, "/grupos/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/etiquetas/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/contatos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/etiquetas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/grupos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/etiquetas/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/grupos/*").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}