package com.ashish.journalApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import com.ashish.journalApp.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    // Pillar 2: Database se user lane wali service (Video me aage banegi)
     @Autowired
     private UserDetailsServiceImpl userDetailsService;

    // =========================================================================
    // 1. REPLACEMENT FOR: configure(HttpSecurity http)
    // =========================================================================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers("/journal/**","/user/**").authenticated() // /journal wale secure rahenge
                        .anyRequest().permitAll())                  // Baaki sab (jaise /user signup) public rahenge
                .httpBasic(Customizer.withDefaults())               // Basic Auth enable kiya
                .csrf(AbstractHttpConfigurer::disable);             // Postman se call ke liye CSRF band kiya

        return http.build();
    }

    // =========================================================================
    // 2. REPLACEMENT FOR: configure(AuthenticationManagerBuilder auth)
    // =========================================================================

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Setter hat gaya hai, ab seedha constructor me pass hota hai:
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Pillar 3: Password Encoder ki Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}