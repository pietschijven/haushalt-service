package de.pschijven.haushaltservice.application;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import com.auth0.client.auth.AuthAPI;
import com.auth0.json.auth.UserInfo;
import com.auth0.jwt.JWT;
import de.pschijven.haushaltservice.configuration.TokenAuthentication;
import de.pschijven.haushaltservice.domain.Auth0Properties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class SecurityController {

    private final AuthenticationController controller;
    private final Auth0Properties properties;
    private final AuthAPI authAPI;

    public SecurityController(AuthenticationController controller, Auth0Properties properties, AuthAPI authAPI) {
        this.controller = controller;
        this.properties = properties;
        this.authAPI = authAPI;
    }

    @GetMapping("/login")
    protected String login(final HttpServletRequest req) {
        String redirectUri = constructRedirectUri(req);
        String authorizeUrl = constructAuthorizeUrl(req, redirectUri);
        return "redirect:" + authorizeUrl;
    }

    @GetMapping("/callback")
    protected void getCallback(final HttpServletRequest req, final HttpServletResponse res)
            throws ServletException, IOException {
        try {
            Tokens tokens = controller.handle(req);
            UserInfo userInfo = authAPI.userInfo(tokens.getAccessToken()).execute();
            String username = userInfo.getValues().get("name").toString();
            TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokens.getIdToken()), userInfo);
            SecurityContextHolder.getContext().setAuthentication(tokenAuth);
            res.sendRedirect("/");
        } catch (AuthenticationException | IdentityVerificationException e) {
            e.printStackTrace();
            SecurityContextHolder.clearContext();
            res.sendRedirect("/login");
        }
    }

    private String constructAuthorizeUrl(HttpServletRequest req, String redirectUri) {
        return controller.buildAuthorizeUrl(req, redirectUri)
                .withAudience(String.format("https://%s/userinfo", properties.getDomain()))
                .withScope("openid profile")
                .build();
    }

    private String constructRedirectUri(HttpServletRequest req) {
        int port = req.getServerPort();
        return req.getScheme() + "://" + req.getServerName() + cleanPort(port) + "/callback";
    }

    private String cleanPort(int port) {
        if (port == 443) {
            return "";
        }
        return ":" + port;
    }
}
