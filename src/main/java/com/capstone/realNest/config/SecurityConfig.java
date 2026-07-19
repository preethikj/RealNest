package com.capstone.realNest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize

                        // Public Thymeleaf pages and resources
                        .requestMatchers("/", "/auth/**", "/properties/**", "/images/**", "/js/**", "/css/**", "/favicon.ico", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Public user APIs
                        .requestMatchers("/api/users/register", "/api/users/forgot-password", "/api/users/reset-password").permitAll()

                        // Public property APIs
                        .requestMatchers(HttpMethod.GET, "/api/properties/search", "/api/properties/*", "/api/properties/*/images").permitAll()

                        // Anyone may send an enquiry.
                        // Logged-in owners are blocked by EnquiryService.
                        .requestMatchers(HttpMethod.POST, "/api/properties/*/enquiries").permitAll()

                        // Admin Thymeleaf pages
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Customer Thymeleaf pages
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")

                        // Admin property APIs
                        .requestMatchers(HttpMethod.PATCH, "/api/properties/*/approve", "/api/properties/*/reject").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/properties/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/properties/status/*").hasRole("ADMIN")

                        // Customer property APIs
                        .requestMatchers(HttpMethod.POST, "/api/properties").hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.PUT, "/api/properties/*").hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.POST, "/api/properties/*/images").hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.DELETE, "/api/properties/*/images/*").hasRole("CUSTOMER")

                        // Owner property list
                        .requestMatchers(HttpMethod.GET, "/api/properties/owner/*").hasAnyRole("CUSTOMER", "ADMIN")

                        // Enquiry viewing APIs
                        .requestMatchers(HttpMethod.GET, "/api/enquiries/**").hasAnyRole("CUSTOMER", "ADMIN")

                        // Admin customer-management APIs
                        // These must appear before /api/users/*
                        .requestMatchers(HttpMethod.GET, "/api/users/customers", "/api/users/customers/count").hasRole("ADMIN")

                        // Authenticated profile APIs
                        .requestMatchers(HttpMethod.GET, "/api/users/*").hasAnyRole("CUSTOMER", "ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/users/*").hasAnyRole("CUSTOMER", "ADMIN")

                        .anyRequest().authenticated())

                .formLogin(form -> form.loginPage("/auth/login").loginProcessingUrl("/auth/login").usernameParameter("username").passwordParameter("password")

                        .successHandler((request, response, authentication) -> {

                            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

                            String targetUrl = isAdmin ? "/admin/dashboard" : "/customer/dashboard";

                            response.sendRedirect(request.getContextPath() + targetUrl);
                        })

                        .failureUrl("/auth/login?error").permitAll())

                .rememberMe(remember -> remember.rememberMeParameter("remember-me").rememberMeCookieName("remember-me").tokenValiditySeconds(7 * 24 * 60 * 60))

                .logout(logout -> logout.logoutUrl("/auth/logout").logoutSuccessUrl("/auth/login?logout").invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID", "remember-me").permitAll());

        return http.build();
    }
}