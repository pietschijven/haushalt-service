package de.pschijven.haushaltservice.configuration;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import de.pschijven.haushaltservice.domain.Auth0Properties;
import de.pschijven.haushaltservice.security.Auth0IdTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@Order(1)
public class Auth0ApiConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private Auth0Properties properties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**");

        // Add ID Token filter at the end of the filter chain
        http.addFilterAfter(new Auth0IdTokenFilter(), FilterSecurityInterceptor.class);

        JwtWebSecurityConfigurer
                .forRS256(properties.getApiAudience(), properties.getApiIssuer())
                .configure(http)
                .authorizeRequests()
                    .antMatchers("/api/transaction").hasAuthority("save:transaction");
    }
}
