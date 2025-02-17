package kls.oauth.authserver.controller;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwkSetRestController {
    @Autowired
    private JWKSet jwkSet;

    @GetMapping("/.key/jwks.json") //TODO
    public Map <String, Object> keys() {
        return this.jwkSet.toJSONObject();
    }
}
