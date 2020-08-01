package kls.oauth.authserver.service;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public class CustomRedisAuthorizationCodeService extends RandomValueAuthorizationCodeServices {
    private static final boolean springDataRedis_2_0 = ClassUtils.isPresent(
            "org.springframework.data.redis.connection.RedisStandaloneConfiguration",
            RedisTokenStore.class.getClassLoader());

    private final RedisConnectionFactory connectionFactory;
    private String prefix = "";
    private static final String CODE = "code:";
    private static final String AUTH = "auth:";
    private static final String CODE_TO_AUTH = "code_to_auth:";
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private Method redisConnectionSet_2_0;
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();
    public void setSerializationStrategy(RedisTokenStoreSerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }
    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        byte[] serializedAuth = serialize(authentication);
        String key = CODE_TO_AUTH + code;
        byte[] codeToAuth = serializeKey(key);

        try (RedisConnection conn = getConnection()) {
            conn.openPipeline();
            try {
                this.redisConnectionSet_2_0.invoke(conn, codeToAuth, serializedAuth);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            conn.closePipeline();
        }
    }

    private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
        return serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        String key = CODE_TO_AUTH + code;
        byte[] codeToAuth = serializeKey(key);
        RedisConnection conn = getConnection();
        OAuth2Authentication authentication;
        try {
            byte[] bytesAuth = conn.get(codeToAuth);
            conn.del(codeToAuth);
            authentication = deserializeAuthentication(bytesAuth);
        }finally {
            conn.close();
        }
        return authentication;
    }


    private RedisConnection getConnection() {
        return connectionFactory.getConnection();
    }

    private byte[] serializeKey(String object) {
        return serialize(prefix + object);
    }

    private byte[] serialize(Object object) {
        return serializationStrategy.serialize(object);
    }

    public CustomRedisAuthorizationCodeService(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        if (springDataRedis_2_0) {
            this.loadRedisConnectionMethods_2_0();
        }
    }

    private void loadRedisConnectionMethods_2_0() {
        this.redisConnectionSet_2_0 = ReflectionUtils.findMethod(
                RedisConnection.class, "set", byte[].class, byte[].class);
    }
}
