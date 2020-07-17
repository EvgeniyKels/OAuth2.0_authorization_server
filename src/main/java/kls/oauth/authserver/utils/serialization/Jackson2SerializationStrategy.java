package kls.oauth.authserver.utils.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import kls.oauth.authserver.utils.serialization.OAuth2AuthenticationDeserializer;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Deserializer;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Serializer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;
import org.springframework.security.web.jackson2.WebJackson2Module;

import java.io.IOException;

public class Jackson2SerializationStrategy extends StandardStringSerializationStrategy {
    private ObjectMapper objectMapper;

    public Jackson2SerializationStrategy() {
        objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(OAuth2Authentication.class, new OAuth2AuthenticationDeserializer());
        objectMapper.registerModule(simpleModule);
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