package de.pschijven.haushaltservice.application;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.pschijven.haushaltservice.domain.TransactionFormBean;
import de.pschijven.haushaltservice.security.AuthenticationJwtWithIdToken;
import de.pschijven.haushaltservice.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HaushaltRestController {

    private final TransactionService service;

    public HaushaltRestController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/api/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> persistTransaction(@RequestBody final TransactionFormBean formBean,
                                                   final Authentication authentication) {
        String username = extractUsername(authentication);
        if (username != null && !username.isEmpty()) {
            service.persistTransaction(formBean.getAmount(), username, formBean.getDescription());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private String extractUsername(final Authentication authentication) {
        if (authentication instanceof AuthenticationJwtWithIdToken) {
            AuthenticationJwtWithIdToken idTokenAuthentication = (AuthenticationJwtWithIdToken) authentication;
            DecodedJWT idToken = idTokenAuthentication.getIdToken();
            Claim usernameClaim = idToken.getClaim("name");
            return usernameClaim.asString();
        }
        return null;
    }
}
