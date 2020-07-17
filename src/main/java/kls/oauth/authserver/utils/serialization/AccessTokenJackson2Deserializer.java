package kls.oauth.authserver.utils.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.oauth2.common.OAuth2AccessToken.*;

public class AccessTokenJackson2Deserializer extends StdDeserializer<OAuth2AccessToken> {

    public AccessTokenJackson2Deserializer() {
        super(OAuth2AccessToken.class);
    }

    protected AccessTokenJackson2Deserializer(Class<OAuth2AccessToken> t) {
        super(t);
    }
    @Override
    public OAuth2AccessToken deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> additionalInfo = new LinkedHashMap<>();
        JsonNode jsonNode = p.getCodec().readTree(p);
        String tokenValue = readJsonNode(jsonNode, ACCESS_TOKEN);
        String tokenType = readJsonNode(jsonNode, TOKEN_TYPE);
        String refreshTokenNode = readJsonNode(jsonNode, REFRESH_TOKEN);
        JsonNode jsonNodeExpiresIn = jsonNode.get(EXPIRES_IN);
        int expiresIn = 0;
        if (jsonNodeExpiresIn != null && !jsonNodeExpiresIn.isEmpty()) {
            expiresIn = jsonNodeExpiresIn.asInt();
        }
        Set<String>scopes = new HashSet<>();
        JsonNode jsonNodeScope = jsonNode.get(SCOPE);
        for (int i = 0; jsonNodeScope.has(i); i++) {
            scopes.add(jsonNodeScope.get(i).asText());
        }
        return new CustomOAuthAccessToken(additionalInfo, scopes, new DefaultOAuth2RefreshToken(refreshTokenNode), tokenType, false, null, expiresIn, tokenValue);
    }

    private String readJsonNode(JsonNode jsonNode, String fieldName) {
        JsonNode innerNode = jsonNode.get(fieldName);
        if (innerNode != null && !innerNode.isEmpty()) {
            return innerNode.asText();
        }
        return null;
    }

    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return null;
    }
}