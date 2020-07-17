package kls.oauth.authserver.utils.serialization;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;
import org.springframework.security.web.jackson2.WebJackson2Module;

import java.io.IOException;

public class Jackson2SerializationStrategy extends StandardStringSerializationStrategy {
    private ObjectMapper objectMapper;

    public Jackson2SerializationStrategy() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.AUTO_DETECT_SETTERS);
        objectMapper.addMixIn(OAuth2AccessToken.class, AccessTokenMixIn.class);
        objectMapper.addMixIn(OAuth2Authentication.class, OAuth2AuthenticationMixin.class);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(OAuth2Authentication.class, new OAuth2AuthenticationDeserializer());
        SimpleModule simpleModule1 = new SimpleModule();
        simpleModule1.addDeserializer(OAuth2AccessToken.class, new AccessTokenJackson2Deserializer(OAuth2AccessToken.class));
        SimpleModule simpleModule2 = new SimpleModule();
        simpleModule2.addSerializer(OAuth2AccessToken.class, new AccessTokenJackson2Serializer(OAuth2AccessToken.class));

        objectMapper.registerModule(simpleModule);
        objectMapper.registerModule(simpleModule1);
        objectMapper.registerModule(simpleModule2);
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