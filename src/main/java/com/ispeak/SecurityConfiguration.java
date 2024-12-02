package com.ispeak;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll() // Permitir acceso a la consola H2
                .antMatchers("/usuario/*").hasRole("ADMIN") // Acceso con rol ADMIN
                .antMatchers("/css/*", "/js/*", "/img/*", "/**", "/main/**", "/usuario/**", "/actividad/**", "/login/**", "/glosario/**").permitAll()
                .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/resolver")
                .failureUrl("/login?error=error")
                .permitAll()
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and().csrf()
                .disable() // Desactivar CSRF para evitar problemas con H2
                .headers().frameOptions().disable(); // Desactivar restricciones de frameOptions para H2
    }

}
