package kls.oauth.authserver.utils.serialization.token;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;

import java.io.IOException;
import java.util.*;

import static org.springframework.security.oauth2.common.OAuth2AccessToken.*;

public class AccessTokenJackson2Deserializer extends StdDeserializer<OAuth2AccessToken> {

    public AccessTokenJackson2Deserializer() {
        super(OAuth2AccessToken.class);
    }

    public AccessTokenJackson2Deserializer(Class<OAuth2AccessToken> t) {
        super(t);
    }

    @Override
    public OAuth2AccessToken deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return deser(p, ctxt, null);
    }

    private OAuth2AccessToken deser(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        Map<String, Object> additionalInfo = new LinkedHashMap<>();
        JsonNode jsonNode = p.getCodec().readTree(p);
        String tokenValue = getStringValue(jsonNode, ACCESS_TOKEN);
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = getDefaultOAuth2AccessToken(additionalInfo, jsonNode, tokenValue);
        String tokenType = getStringValue(jsonNode, TOKEN_TYPE);
        String refreshToken = getStringValue(jsonNode, REFRESH_TOKEN);
        DefaultOAuth2RefreshToken defaultOAuth2RefreshToken = new DefaultOAuth2RefreshToken(refreshToken);
        JsonNode jsonNodeExpiresIn = jsonNode.get(EXPIRES_IN);
        int expiresIn = 0;
        if (jsonNodeExpiresIn != null && !jsonNodeExpiresIn.isMissingNode()) {
            expiresIn = jsonNodeExpiresIn.asInt();
        }
        Set<String> scopes = new HashSet<>();
        JsonNode jsonNodeScope = jsonNode.get(SCOPE);
        if (jsonNodeScope != null && !jsonNodeScope.isMissingNode()) {
            if (jsonNodeScope.isArray()) {
                String scope = jsonNodeScope.asText();
                scopes.add(scope);
            } else {
                scopes = OAuth2Utils.parseParameterList(jsonNodeScope.asText());
            }
        }

        defaultOAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + expiresIn * 1000));
        defaultOAuth2AccessToken.setRefreshToken(defaultOAuth2RefreshToken);
        defaultOAuth2AccessToken.setTokenType(tokenType);
        defaultOAuth2AccessToken.setScope(scopes);
        defaultOAuth2AccessToken.setAdditionalInformation(additionalInfo);
        return defaultOAuth2AccessToken;
    }

    private DefaultOAuth2AccessToken getDefaultOAuth2AccessToken(Map<String, Object> additionalInfo, JsonNode jsonNode, String tokenValue) {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(tokenValue);
        Iterator<String> fieldNames = jsonNode.fieldNames();
        while (fieldNames.hasNext()) {
            String field = fieldNames.next();
            if(!field.equals(ACCESS_TOKEN)
                    && !field.equals(EXPIRES_IN)
                    && !field.equals(REFRESH_TOKEN)
                    && !field.equals(SCOPE)
                    && !field.equals(TOKEN_TYPE)
                    && !field.equals("@class")) {
                additionalInfo.put(field, jsonNode.get(field));
            }
        }
        return defaultOAuth2AccessToken;
    }

    private String getStringValue(JsonNode jsonNode, String fieldName) {
        String value = null;
        JsonNode stringJsonNode = jsonNode.get(fieldName);
        if (stringJsonNode != null && !stringJsonNode.isMissingNode()) {
            value = stringJsonNode.asText();
        }
        return value;
    }

    @Override
    public OAuth2AccessToken deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return deser(p, ctxt, typeDeserializer);
    }
}