package org.example.a2024_05_sopra_server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
open class SpringSecurityConfig {

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests()
            .requestMatchers("/testPrivate").authenticated()
            .requestMatchers("/testPrivateAdmin").hasRole("ADMIN")
            .anyRequest().permitAll()
            .and().formLogin()
            .and().oauth2Login()
            .and().build();
    }

    @Autowired
    open fun configureGlobal(auth : AuthenticationManagerBuilder) {
        val encoder = BCryptPasswordEncoder()

        //Créer des utilisateurs fixes
        auth.inMemoryAuthentication()
            .passwordEncoder(encoder)
            .withUser("aaa")
            .password(encoder.encode("aaa"))
            .roles("USER")
            .and()
            .withUser("Admin")
            .password(encoder.encode("Admin"))
            .roles("ADMIN")
    }
}