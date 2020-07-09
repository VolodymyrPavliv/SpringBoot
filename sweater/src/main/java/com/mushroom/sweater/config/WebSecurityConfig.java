package com.mushroom.sweater.config;

import com.mushroom.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//Включення авторезації
                    .antMatchers("/", "/registration", "/static").permitAll()//Надається повний доступ до запитиу за алресою '/'
                    .anyRequest().authenticated() //до інших запитів вимагається авторизія.
                .and()
                    .formLogin()//Включення форми login
                    .loginPage("/login")
                    .permitAll() //Надається повний доступ до запитиу за алресою '/login'
                .and()
                    .logout()
                    .permitAll(); //Включення logout та надання повного доступу до нього.
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
   }
}
