package de.pschijven.haushaltservice.application;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
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

    public SecurityController(AuthenticationController controller, Auth0Properties properties) {
        this.controller = controller;
        this.properties = properties;
    }

    @GetMapping("/login")
    protected String login(final HttpServletRequest req) {
        String redirectUri = req.getScheme() + "://" + req.getServerName() + "/callback";
        String authorizeUrl = controller.buildAuthorizeUrl(req, redirectUri)
                .withAudience(String.format("https://%s/userinfo", properties.getDomain()))
                .build();
        return "redirect:" + authorizeUrl;
    }

    @GetMapping("/callback")
    protected void getCallback(final HttpServletRequest req, final HttpServletResponse res)
            throws ServletException, IOException {
        try {
            Tokens tokens = controller.handle(req);
            TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokens.getIdToken()));
            SecurityContextHolder.getContext().setAuthentication(tokenAuth);
            res.sendRedirect("/");
        } catch (AuthenticationException | IdentityVerificationException e) {
            e.printStackTrace();
            SecurityContextHolder.clearContext();
            res.sendRedirect("/login");
        }
    }
}
