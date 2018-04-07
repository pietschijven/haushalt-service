package de.pschijven.haushaltservice.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.spring.security.api.authentication.AuthenticationJsonWebToken;
import com.auth0.spring.security.api.authentication.JwtAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Delegate object that stores an additional ID token next to the decoded access token.
 * Used in the security for the REST API.
 */
public class AuthenticationJwtWithIdToken implements Authentication, JwtAuthentication {

    private final AuthenticationJsonWebToken accessTokenAuthentication;
    private DecodedJWT idToken;

    public AuthenticationJwtWithIdToken(AuthenticationJsonWebToken accessTokenAuthentication) {
        this.accessTokenAuthentication = accessTokenAuthentication;
    }

    public DecodedJWT getIdToken() {
        return idToken;
    }

    public void setIdToken(DecodedJWT idToken) {
        this.idToken = idToken;
    }

    @Override
    public String getToken() {
        return accessTokenAuthentication.getToken();
    }

    @Override
    public String getKeyId() {
        return accessTokenAuthentication.getKeyId();
    }

    @Override
    public Authentication verify(JWTVerifier verifier) throws JWTVerificationException {
        return accessTokenAuthentication.verify(verifier);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return accessTokenAuthentication.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return accessTokenAuthentication.getCredentials();
    }

    @Override
    public Object getDetails() {
        return accessTokenAuthentication.getDetails();
    }

    @Override
    public Object getPrincipal() {
        return accessTokenAuthentication.getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        return accessTokenAuthentication.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        accessTokenAuthentication.setAuthenticated(b);
    }

    @Override
    public String getName() {
        return accessTokenAuthentication.getName();
    }
}
