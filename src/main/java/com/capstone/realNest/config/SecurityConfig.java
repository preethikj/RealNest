package com.capstone.realNest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(
                                "/",
                                "/auth/**",
                                "/properties/**",
                                "/images/**",
                                "/js/**",
                                "/css/**",
                                "/favicon.ico",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/customer/**")
                        .hasRole("CUSTOMER")

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("username")
                        .passwordParameter("password")

                        .successHandler((request, response, authentication) -> {

                            boolean isAdmin = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(authority ->
                                            authority.getAuthority()
                                                    .equals("ROLE_ADMIN")
                                    );

                            String targetUrl = isAdmin ? "/admin/dashboard" : "/customer/dashboard";

                            response.sendRedirect(request.getContextPath() + targetUrl);
                        })

                        .failureUrl("/auth/login?error")
                        .permitAll()
                )

                .rememberMe(remember -> remember
                        .rememberMeParameter("remember-me")
                        .rememberMeCookieName("remember-me")
                        .tokenValiditySeconds(7 * 24 * 60 * 60))

                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies(
                                "JSESSIONID",
                                "remember-me"
                        )
                        .permitAll());

        return http.build();
    }
}