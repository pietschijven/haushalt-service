package de.pschijven.haushaltservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.spring.security.api.authentication.AuthenticationJsonWebToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Extracts an Auth0 id token from the http request.
 */
public class Auth0IdTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication instanceof AuthenticationJsonWebToken) {
            AuthenticationJsonWebToken jwtAuth = (AuthenticationJsonWebToken) authentication;
            // Test if an ID token is also being sent
            String idToken = request.getHeader("X-AUTH0-ID-TOKEN");
            if (idToken != null) {
                DecodedJWT decodedIdToken = JWT.decode(idToken);
                AuthenticationJwtWithIdToken auth = new AuthenticationJwtWithIdToken(jwtAuth);
                auth.setIdToken(decodedIdToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }
        filterChain.doFilter(request, response);
    }

}
