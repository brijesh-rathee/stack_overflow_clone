package com.stackclone.stackoverflow_clone.config;

import com.stackclone.stackoverflow_clone.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/auth/**","/", "/home", "/tags/**").permitAll()
                                .requestMatchers("/user/**").hasAnyAuthority(UserRole.USER.name())
                                .requestMatchers("/admin/**").hasAnyAuthority(UserRole.ADMIN.name())
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .defaultSuccessUrl("/",true)
                        .failureUrl("/auth/login?error")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}