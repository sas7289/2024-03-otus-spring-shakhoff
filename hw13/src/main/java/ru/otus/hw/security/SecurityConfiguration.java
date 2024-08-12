package ru.otus.hw.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf().disable()
            .authorizeHttpRequests( ( authorize ) -> authorize
                .antMatchers( "/**", "/" ).permitAll()
            )
            .formLogin();
//                                .csrf(AbstractHttpConfigurer::disable)
//                                .headers(AbstractHttpConfigurer::disable)
//                                .formLogin(Customizer.withDefaults())
//                                .authorizeHttpRequests(authorize -> authorize
//                                    .anyRequest().authenticated());
//                .requestMatchers("/books/**").hasAnyRole("admin", "user")
//                .and().logout()
//                .anyRequest().denyAll());
//                .anyRequest().permitAll());
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}