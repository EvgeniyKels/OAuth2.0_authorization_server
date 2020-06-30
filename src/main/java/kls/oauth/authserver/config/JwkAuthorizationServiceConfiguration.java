package kls.oauth.authserver.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import kls.oauth.authserver.model.repos.IAccessTokenRepository;
import kls.oauth.authserver.model.repos.IRefreshTokenRepository;
import kls.oauth.authserver.service.CustomAuthDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Map;

@Configuration
@EnableAuthorizationServer
public class JwkAuthorizationServiceConfiguration extends AuthorizationServerConfigurerAdapter {
    private final String keyFilePath = "kls-jwt.jks";
    private final String password = "kls-pass";
    private final String key = "kls-oauth-jwt";
    private final String keyId = "kls_key_id";

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IAccessTokenRepository accessTokenRepository;
    @Autowired
    private IRefreshTokenRepository refreshTokenRepository;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomAuthDetailsService customUserDetailsService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("denyAll()").checkTokenAccess("isAuthenticated()");
    }

    @Bean
    /*
      holder for private and public keys
     */
    public KeyPair keyPair() {
        ClassPathResource keyStoreFile = new ClassPathResource(keyFilePath);
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyStoreFile, password.toCharArray());
        return keyStoreKeyFactory.getKeyPair(key);
    }

    @Bean
    /*
        create a set of keys for endpoint /.well-known/jwks.json
     */
    public JWKSet jwkSet() {
        RSAKey.Builder builder =
                new RSAKey.Builder((RSAPublicKey)keyPair().getPublic())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(keyId);
        return new JWKSet(builder.build());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(customUserDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.
                authenticationManager(authenticationManager).
                tokenStore(tokenStore()).
                accessTokenConverter(tokenEnhancer());
    }

//    @Bean
//    public JwtTokenStore tokenStore() {
//        return new JwtTokenStore(tokenEnhancer());
//    }

    @Bean
    public TokenStore tokenStore() {
        return new CustomTokenStore(accessTokenRepository, refreshTokenRepository);
    }

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        Map <String, String> customHeaders =
                Collections.singletonMap("kid", keyId);
        return new JWTCustomAccessTokenConverter(customHeaders, keyPair());
    }
}