package mk.finki.ukim.lab2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig{

    private final PasswordEncoder passwordEncoder;
    private final CustomUsernamePasswordAuthenticationProvider customAuthenticationProvider;

    public WebSecurityConfig(PasswordEncoder passwordEncoder, CustomUsernamePasswordAuthenticationProvider customAuthenticationProvider) {
        this.passwordEncoder = passwordEncoder;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/assets/**", "/register", "/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Only Admins can access /admin/**
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")  // Admins and Managers can access /manager/**
                        .requestMatchers("/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")  // Users, Managers, and Admins can access /user/**
                        .anyRequest().authenticated())  // All other requests need authentication
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .failureUrl("/login?error=BadCredentials")
                        .defaultSuccessUrl("/home", true))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login"))
                .exceptionHandling(ex -> ex.accessDeniedPage("/access_denied"))
                .sessionManagement(session -> session
                        .maximumSessions(1)  // Single session per user
                        .expiredUrl("/login?error=sessionExpired"));  // Redirect to login page when session expires

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .roles("USER")
                .build();
        UserDetails manager = User.builder()
                .username("manager")
                .password(passwordEncoder.encode("manager"))
                .roles("MANAGER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, manager, admin);
    }


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);

        return authenticationManagerBuilder.build();
    }
}



