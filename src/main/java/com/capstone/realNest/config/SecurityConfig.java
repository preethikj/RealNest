package com.capstone.realNest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                /*
                 * Swagger uses HTTP Basic authentication for REST APIs.
                 * Thymeleaf forms retain normal CSRF protection.
                 */
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )

                .authorizeHttpRequests(authorize -> authorize

                        // Public pages, resources and API documentation.
                        .requestMatchers(
                                "/",
                                "/auth/**",
                                "/properties/**",

                                "/images/**",
                                "/js/**",
                                "/css/**",
                                "/favicon.ico",

                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",

                                "/error"
                        ).permitAll()

                        // Public registration and password recovery.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users/register",
                                "/api/users/forgot-password",
                                "/api/users/reset-password"
                        ).permitAll()

                        // Public property browsing.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/properties/search",
                                "/api/properties/*",
                                "/api/properties/*/images"
                        ).permitAll()

                        // Visitors may send property enquiries.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/properties/*/enquiries"
                        ).permitAll()

                        // Admin property-management APIs.
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/properties/*/approve",
                                "/api/properties/*/reject"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/properties/*"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/properties/status/*"
                        ).hasRole("ADMIN")

                        // Customer property-management APIs.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/properties"
                        ).hasRole("CUSTOMER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/properties/*"
                        ).hasRole("CUSTOMER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/properties/*/images"
                        ).hasRole("CUSTOMER")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/properties/*/images/*"
                        ).hasRole("CUSTOMER")

                        // Customer and Admin shared APIs.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/properties/owner/*",
                                "/api/enquiries/**"
                        ).hasAnyRole("CUSTOMER", "ADMIN")

                        // Admin customer-management APIs.
                        // Keep these before the general user-ID rules.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/customers",
                                "/api/users/customers/count"
                        ).hasRole("ADMIN")

                        // Authenticated profile APIs.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/*"
                        ).hasAnyRole("CUSTOMER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/users/*"
                        ).hasAnyRole("CUSTOMER", "ADMIN")

                        // Thymeleaf pages protected by role.
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/customer/**")
                        .hasRole("CUSTOMER")

                        .anyRequest()
                        .authenticated()
                )

                // Enables Swagger's email/password authorization.
                .httpBasic(Customizer.withDefaults())

                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("username")
                        .passwordParameter("password")

                        .successHandler(
                                (request, response, authentication) -> {

                                    boolean isAdmin =
                                            authentication
                                                    .getAuthorities()
                                                    .stream()
                                                    .anyMatch(authority ->
                                                            authority
                                                                    .getAuthority()
                                                                    .equals(
                                                                            "ROLE_ADMIN"
                                                                    )
                                                    );

                                    String targetUrl =
                                            isAdmin
                                                    ? "/admin/dashboard"
                                                    : "/customer/dashboard";

                                    response.sendRedirect(
                                            request.getContextPath()
                                                    + targetUrl
                                    );
                                }
                        )

                        .failureUrl("/auth/login?error")
                        .permitAll()
                )

                .rememberMe(remember -> remember
                        .rememberMeParameter("remember-me")
                        .rememberMeCookieName("remember-me")
                        .tokenValiditySeconds(
                                7 * 24 * 60 * 60
                        )
                )

                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl(
                                "/auth/login?logout"
                        )
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies(
                                "JSESSIONID",
                                "remember-me"
                        )
                        .permitAll()
                );

        return http.build();
    }
}