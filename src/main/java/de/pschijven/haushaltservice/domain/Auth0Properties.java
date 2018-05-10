package de.pschijven.haushaltservice.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "com.auth0")
public class Auth0Properties {

    @NotNull
    private String domain;

    @NotNull
    private String clientId;

    @NotNull
    private String clientSecret;

    @NotNull
    private String apiAudience;

    @NotNull
    private String apiIssuer;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getApiAudience() {
        return apiAudience;
    }

    public void setApiAudience(String apiAudience) {
        this.apiAudience = apiAudience;
    }

    public String getApiIssuer() {
        return apiIssuer;
    }

    public void setApiIssuer(String apiIssuer) {
        this.apiIssuer = apiIssuer;
    }
}
