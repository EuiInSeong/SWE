package com.commitfarm.farm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(
                                "/v3/api-docs/**",  // springdoc-openapi 2.x 사용 시 변경된 경로
                                "/swagger-ui.html",
                                "/swagger-ui/**", "*").permitAll()  // Swagger 관련 URL을 인증 없이 접근 허용

                        .anyRequest().authenticated()
                );
//                .formLogin((form) -> form
//                        .loginPage("/login")
////                        .permitAll()
////                )
////                .logout((logout) -> logout
////                        .permitAll()
////                );

        return http.build();
    }
}
