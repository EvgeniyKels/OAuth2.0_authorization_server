package kls.oauth.authserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.codehaus.jackson.map.JsonDeserializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson1Serializer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;
import org.springframework.security.web.jackson2.WebJackson2Module;

import java.io.IOException;

public class Jackson2SerializationStrategy extends StandardStringSerializationStrategy {
    private ObjectMapper objectMapper;
    Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
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
        System.out.println(new String(bytes));
        System.out.println(clazz.getSimpleName());
        T t = null;
        try {
            t = objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    protected byte[] serializeInternal(Object object) {
//        byte[] serialize = serializer.serialize(object);
//        byte[] bytes = new byte[0];
//        try {
//            bytes = objectMapper.writeValueAsString(object).getBytes();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return bytes;
        return serializer.serialize(object);
    }
//
//    public Jackson2JsonRedisSerializer getSerializer() {
//        return serializer;
//    }
//
//    public void setSerializer(Jackson2JsonRedisSerializer serializer) {
//        this.serializer = serializer;
//    }
}
