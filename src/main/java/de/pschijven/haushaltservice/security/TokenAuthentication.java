package de.pschijven.haushaltservice.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private final DecodedJWT idToken;
    private final String accessToken;
    private boolean invalidated;

    public TokenAuthentication(final String accessToken, final DecodedJWT idToken) {
        super(readAuthorities(idToken));
        this.idToken = idToken;
        this.accessToken = accessToken;
    }

    private boolean hasExpired() {
        return idToken.getExpiresAt().before(new Date());
    }

    private static Collection<? extends GrantedAuthority> readAuthorities(final DecodedJWT idToken) {
        Claim rolesClaim = idToken.getClaim("https://access.control/roles");
        if (rolesClaim.isNull()) {
            return Collections.emptyList();
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] scopes = rolesClaim.asArray(String.class);
        for (String s : scopes) {
            SimpleGrantedAuthority a = new SimpleGrantedAuthority(s);
            if (!authorities.contains(a)) {
                authorities.add(a);
            }
        }
        return authorities;
    }


    @Override
    public String getCredentials() {
        return idToken.getToken();
    }

    @Override
    public Object getPrincipal() {
        return idToken.getSubject();
    }

    public String getUsername() {
        return idToken.getClaim("name").asString();
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Create a new Authentication object to authenticate");
        }
        invalidated = true;
    }

    @Override
    public boolean isAuthenticated() {
        return !invalidated && !hasExpired();
    }

    public Map<String, Object> getUserMetadata() {
        Claim claim = idToken.getClaim("https://immense-taiga-71072.herokuapp.com/user_metadata");
        if (claim != null) {
            return claim.asMap();
        }
        return new HashMap<>();
    }

    public String getAccessToken() {
        return accessToken;
    }
}
