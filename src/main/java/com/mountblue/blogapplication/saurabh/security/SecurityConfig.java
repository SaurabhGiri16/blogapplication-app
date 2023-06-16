package com.mountblue.blogapplication.saurabh.security;

import com.mountblue.blogapplication.saurabh.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeRequests()
                .requestMatchers("/", "post{postId}","/api/blog-app/",  "/api/blog-app/posts/{postId}", "/register", "/api/blog-app/posts/{postId}/comments", "/addComment{postId}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/blog-app/user","/api/blog-app/posts/{postId}/comments").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                ).csrf(AbstractHttpConfigurer::disable)
                .build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new
                DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }
}
