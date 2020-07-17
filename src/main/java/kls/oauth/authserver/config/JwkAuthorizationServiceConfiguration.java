package kls.oauth.authserver.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import kls.oauth.authserver.security.JWTCustomAccessTokenConverter;
import kls.oauth.authserver.service.CustomAuthDetailsService;
import kls.oauth.authserver.utils.serialization.Jackson2SerializationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Map;

@Configuration
@EnableAuthorizationServer
public class JwkAuthorizationServiceConfiguration extends AuthorizationServerConfigurerAdapter {
    private String keyFilePath = "kls-jwt.jks";
    private String password = "kls-pass";
    private String key = "kls-oauth-jwt";
    private String keyId = "kls_key_id";

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomAuthDetailsService customUserDetailsService;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
        security.tokenKeyAccess("denyAll()").checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public KeyPair keyPair() {
        ClassPathResource keyStoreFile = new ClassPathResource(keyFilePath);
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyStoreFile, password.toCharArray());
        return keyStoreKeyFactory.getKeyPair(key);
    }

    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey)keyPair().getPublic())
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

//    @Bean
//    public TokenStore tokenStore() {
//        return new MongoTokenStore();
//    }

    @Bean
    public TokenStore tokenStore() {
        Jackson2SerializationStrategy jackson2SerializationStrategy = new Jackson2SerializationStrategy();
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setSerializationStrategy(jackson2SerializationStrategy);
        return redisTokenStore;
    }

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        Map <String, String> customHeaders =
                Collections.singletonMap("kid", keyId);
        return new JWTCustomAccessTokenConverter(customHeaders, keyPair());
    }
}