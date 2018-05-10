package de.pschijven.haushaltservice.configuration;

import com.auth0.AuthenticationController;
import com.auth0.client.auth.AuthAPI;
import de.pschijven.haushaltservice.domain.Auth0Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class Auth0Configuration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/callback", "/login").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                .and().logout().permitAll();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
        http.csrf()
                .ignoringAntMatchers("/api/**");
    }

    @Bean
    public AuthenticationController authenticationController(final Auth0Properties properties) {
        return AuthenticationController
                .newBuilder(properties.getDomain(), properties.getClientId(), properties.getClientSecret())
                .build();
    }

    @Bean
    public AuthAPI authAPI(final Auth0Properties properties) {
        return new AuthAPI(properties.getDomain(), properties.getClientId(), properties.getClientSecret());
    }
}
