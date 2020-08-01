package kls.oauth.authserver.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import kls.oauth.authserver.security.JWTCustomAccessTokenConverter;
import kls.oauth.authserver.service.CustomAuthDetailsService;
import kls.oauth.authserver.service.CustomRedisAuthorizationCodeService;
import kls.oauth.authserver.utils.serialization.Jackson2SerializationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Map;

@Configuration
@EnableAuthorizationServer
public class JwkAuthorizationServiceConfiguration extends AuthorizationServerConfigurerAdapter {
    @Value("${jks.path}")
    private String keyFilePath;
    @Value("${jks.password}")
    private String password;
    @Value("${jks.key}")
    private String key;
    @Value("${jks.keyId}")
    private String keyId;

    @Autowired
    DataSource dataSource;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomAuthDetailsService customUserDetailsService;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    private final Jackson2SerializationStrategy jackson2SerializationStrategy = new Jackson2SerializationStrategy();
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("denyAll()").checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public KeyPair keyPair() {
        FileUrlResource keyStoreFile = null;
        try {
            keyStoreFile = new FileUrlResource(keyFilePath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
                authenticationManager(authenticationManager).authorizationCodeServices(codeServices()).
                tokenStore(tokenStore()).reuseRefreshTokens(false).
                accessTokenConverter(tokenEnhancer());
    }

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setSerializationStrategy(jackson2SerializationStrategy);
        return redisTokenStore;
    }

//    @Bean
//    public AuthorizationCodeServices codeServices() {
//        return new JdbcAuthorizationCodeServices(dataSource);
//    }

    @Bean
    public AuthorizationCodeServices codeServices() {
        CustomRedisAuthorizationCodeService customRedisAuthorizationCodeService = new CustomRedisAuthorizationCodeService(redisConnectionFactory);
        customRedisAuthorizationCodeService.setSerializationStrategy(jackson2SerializationStrategy);
        return customRedisAuthorizationCodeService;
    }

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        Map <String, String> customHeaders =
                Collections.singletonMap("kid", keyId);
        return new JWTCustomAccessTokenConverter(customHeaders, keyPair());
    }

//    @Bean
//    public JwtTokenStore tokenStore() {
//        return new JwtTokenStore(tokenEnhancer());
//    }

//    @Bean
//    public TokenStore tokenStore() {
//        return new MongoTokenStore();
//    }

}