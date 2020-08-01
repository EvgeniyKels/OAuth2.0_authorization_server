package kls.oauth.authserver.utils.serialization.token;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class AccessTokenJackson2Serializer extends StdSerializer<OAuth2AccessToken> {

    public AccessTokenJackson2Serializer(Class<OAuth2AccessToken> t) {
        super(t);
    }

    public AccessTokenJackson2Serializer() {
        super(OAuth2AccessToken.class);
    }

    @Override
    public void serialize(OAuth2AccessToken value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        customSerialization(value, gen, provider, null);
    }

    @Override
    public void serializeWithType(OAuth2AccessToken value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        customSerialization(value, gen, serializers, typeSer);
    }

    private void customSerialization(OAuth2AccessToken token, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        gen.writeStartObject();
        if(typeSer != null) {
            gen.writeStringField(typeSer.getPropertyName(), token.getClass().getName());
        }
        gen.writeStringField(OAuth2AccessToken.ACCESS_TOKEN, token.getValue());
        gen.writeStringField(OAuth2AccessToken.TOKEN_TYPE, token.getTokenType());
        OAuth2RefreshToken refreshToken = token.getRefreshToken();
        if (refreshToken != null) {
            gen.writeStringField(OAuth2AccessToken.REFRESH_TOKEN, refreshToken.getValue());
        }
        Date expiration = token.getExpiration();
        if (expiration != null) {
            long now = System.currentTimeMillis();
            gen.writeNumberField(OAuth2AccessToken.EXPIRES_IN, (expiration.getTime() - now) / 1000);
        }
        Set<String> scopes = token.getScope();
        if (scopes != null && !scopes.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String scope: scopes) {
                sb.append(scope);
                sb.append(" ");
            }
            String substring = sb.substring(0, sb.length() - 1);
            gen.writeStringField(OAuth2AccessToken.SCOPE, substring);
        }
        Map<String, Object> additionalInformation = token.getAdditionalInformation();
        if (additionalInformation != null && !additionalInformation.isEmpty()) {
            Set<Map.Entry<String, Object>> entries = additionalInformation.entrySet();
            for (Map.Entry<String, Object> entry: entries) {
                String key = entry.getKey();
                Object value = entry.getValue();
                gen.writeObjectField(key, value);
            }
        }
        gen.writeEndObject();
    }
}