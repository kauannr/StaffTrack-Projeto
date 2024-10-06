package com.example.springthymeleaf.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.springthymeleaf.service.ImplemUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity {

    @Autowired
    private ImplemUserDetailsService implemUserDetailsService;

    @Autowired
    private AcessDanied acessDanied;

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/").permitAll() // Permite acesso público à página inicial
                .requestMatchers("/materialize/**").permitAll()
                .requestMatchers("/telefones/{idPessoa}").hasAnyRole("MANAGER")
                .anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and()
                .exceptionHandling()
                .accessDeniedHandler(acessDanied);

        return http.build();
    }

    @Autowired
    public void config(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(implemUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

}
