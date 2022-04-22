package com.omarserrar.textme.configurations;

import com.omarserrar.textme.configurations.filters.JWTFilter;
import com.omarserrar.textme.models.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private JWTFilter jwtFilter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .cors().configurationSource(request -> {
                    CorsConfiguration c = new CorsConfiguration().applyPermitDefaultValues();
                    c.addAllowedMethod(HttpMethod.DELETE);
                    return c;
                }).and()
                .csrf().disable()
                .authorizeRequests().antMatchers("/api/auth/login", "/api/auth/register").not().authenticated()
                .antMatchers("/stomp").permitAll()
                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService((username)->
                userRepository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("Username not found"))
        ).passwordEncoder(passwordEncoder);
    }

}
