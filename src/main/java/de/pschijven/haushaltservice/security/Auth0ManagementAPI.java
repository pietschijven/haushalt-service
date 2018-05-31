package de.pschijven.haushaltservice.security;

import com.auth0.json.auth.TokenHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.pschijven.haushaltservice.domain.Auth0Properties;
import de.pschijven.haushaltservice.domain.UserMetadata;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Auth0ManagementAPI {

    private final Auth0Properties properties;
    private final RestTemplate restTemplate;

    public Auth0ManagementAPI(Auth0Properties properties) {
        this.properties = properties;
        this.restTemplate = new RestTemplate();
    }

    public List<UserMetadata> fetchUserMetadata() {
        try {
            String accessToken = getManagementAccessToken();
            String response = requestUserMetadata(accessToken);
            return extractMetadataFromResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<UserMetadata> extractMetadataFromResponse(String response) throws IOException {
        List<UserMetadata> userMetadata = new ArrayList<>();
        JsonNode jsonNode = new ObjectMapper().readTree(response);
        for (int i = 0; i < jsonNode.size(); i++) {
            JsonNode element = jsonNode.get(i);
            String name = element.get("name").textValue();
            JsonNode metadata = element.get("user_metadata");
            BigDecimal salary = metadata.get("salary").decimalValue();
            userMetadata.add(new UserMetadata(name, salary));
        }
        return userMetadata;
    }

    private String requestUserMetadata(String accessToken) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + accessToken);

        String url = properties.getManagementAudience() + "users";
        return restTemplate
                .exchange(url,HttpMethod.GET, new HttpEntity<String>(null, headers), String.class)
                .getBody();
    }

    private String getManagementAccessToken() throws JsonProcessingException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Content-Type", "application/json");

        Map<String, String> body = new HashMap<>();
        body.put("client_id", properties.getManagementClientId());
        body.put("client_secret", properties.getManagementClientSecret());
        body.put("grant_type", "client_credentials");
        body.put("audience", properties.getManagementAudience());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode js = mapper.convertValue(body, JsonNode.class);
        String json = mapper.writeValueAsString(js);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<TokenHolder> holder = restTemplate
                .exchange(tokenEndpoint(), HttpMethod.POST, entity, TokenHolder.class);

        return holder.getBody().getAccessToken();
    }

    private String tokenEndpoint() {
        return "https://app86860431.auth0.com/oauth/token";
    }
}
