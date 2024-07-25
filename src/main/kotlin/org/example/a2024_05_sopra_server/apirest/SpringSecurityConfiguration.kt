package org.example.a2024_05_sopra_server.apirest

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
        http.authorizeHttpRequests { authorize ->
            authorize.requestMatchers("/testPrivate").authenticated()
            authorize.requestMatchers("/testPrivateAdmin").hasRole("ADMIN")
            authorize.requestMatchers("/ws/**").authenticated()
//            authorize.requestMatchers("/tchat/saveMessage").authenticated()
//            authorize.requestMatchers("/tchat/allMessages").authenticated()
                .anyRequest().permitAll()
        }
            .httpBasic { }
            .formLogin { }
        return http.build()
    }

    @Autowired
    open fun configureGlobal(auth: AuthenticationManagerBuilder) {
        val encoder = BCryptPasswordEncoder()

        //Créer des utilisateurs fixes
        auth.inMemoryAuthentication()
            .passwordEncoder(encoder)
            .withUser("aaa")
            .password(encoder.encode("bbb"))
            .roles("USER")
            .and()
            .withUser("Admin")
            .password(encoder.encode("Admin"))
            .roles("ADMIN")
    }

}