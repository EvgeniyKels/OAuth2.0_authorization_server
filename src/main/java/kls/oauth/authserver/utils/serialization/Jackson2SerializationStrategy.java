package kls.oauth.authserver.utils.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import kls.oauth.authserver.utils.serialization.authenticatioin.OAuth2AuthenticationDeserializer;
import kls.oauth.authserver.utils.serialization.token.mixins.AccessTokenMixIn;
import kls.oauth.authserver.utils.serialization.authenticatioin.mixins.OAuth2AuthenticationMixin;
import kls.oauth.authserver.utils.serialization.token.AccessTokenJackson2Deserializer;
import kls.oauth.authserver.utils.serialization.token.AccessTokenJackson2Serializer;
import kls.oauth.authserver.utils.serialization.token.mixins.DefaultExpiringOAuth2RefreshTokenMixIn;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;
import org.springframework.security.web.jackson2.WebJackson2Module;

import java.io.IOException;

public class Jackson2SerializationStrategy extends StandardStringSerializationStrategy {
    private final ObjectMapper objectMapper;

    public Jackson2SerializationStrategy() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.AUTO_DETECT_SETTERS);
        objectMapper.addMixIn(DefaultExpiringOAuth2RefreshToken.class, DefaultExpiringOAuth2RefreshTokenMixIn.class);
        objectMapper.addMixIn(OAuth2AccessToken.class, AccessTokenMixIn.class);
        objectMapper.addMixIn(OAuth2Authentication.class, OAuth2AuthenticationMixin.class);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(OAuth2Authentication.class, new OAuth2AuthenticationDeserializer());
        SimpleModule simpleModule1 = new SimpleModule();
        simpleModule1.addDeserializer(OAuth2AccessToken.class, new AccessTokenJackson2Deserializer(OAuth2AccessToken.class));
        simpleModule1.addSerializer(OAuth2AccessToken.class, new AccessTokenJackson2Serializer(OAuth2AccessToken.class));

        objectMapper.registerModule(simpleModule);
        objectMapper.registerModule(simpleModule1);
        objectMapper.registerModule(new CoreJackson2Module());
        objectMapper.registerModule(new WebJackson2Module());
    }

    @Override
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        T t = null;
        try {
            t = objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
//    Type id handling
//    not implemented for type
//    org.springframework.security.oauth2.common.OAuth2AccessToken
//            (by serializer of type org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Serializer
    @Override
    protected byte[] serializeInternal(Object object) {
        byte[] byteArr = null;
        try {
            byteArr = objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return byteArr;
    }
}