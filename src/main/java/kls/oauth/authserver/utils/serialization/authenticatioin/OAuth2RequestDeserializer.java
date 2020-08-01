package kls.oauth.authserver.utils.serialization.authenticatioin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class OAuth2RequestDeserializer extends JsonDeserializer<OAuth2Request> {
    @Override
    public OAuth2Request deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return deser(p, ctxt, null);
    }

    @Override
    public OAuth2Request deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return deser(p, ctxt, typeDeserializer);
    }

    private OAuth2Request deser(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        JsonNode jsonNode = p.getCodec().readTree(p);
        String clientId = getString(jsonNode.get("clientId"));
        JsonNode approvedNode = jsonNode.get("approved");
        boolean approved = false;
        if (approvedNode != null && !approvedNode.isMissingNode()) {
            approved = approvedNode.asBoolean();
        }
        String redirectUri = getString(jsonNode.get("redirectUri"));
        Set<String> resourceIds = getSetFromJsonNode(jsonNode.get("resourceIds").get(1));
        Set<String> responseTypes = getSetFromJsonNode(jsonNode.get("responseTypes").get(1));
        Map<String, Serializable> extensionProperties = new HashMap<>();
        getMapFromJsonNode(jsonNode.get("extensions").get(1), extensionProperties);
        Map<String, String> requestParameters = new LinkedHashMap<>();
        getMapFromJsonNode(jsonNode.get("requestParameters"), requestParameters);

        JsonNode authoritiesNode = jsonNode.get("authorities").get(1);
        Collection<? extends GrantedAuthority> authorities = null;
        if (authoritiesNode != null && !authoritiesNode.isEmpty()) {
            Set<SimpleGrantedAuthority>roleSet = new HashSet<>();
            for (int i = 0; authoritiesNode.has(i); i++) {
                roleSet.add(new SimpleGrantedAuthority(authoritiesNode.get(i).asText()));
            }
            authorities = new HashSet<>(roleSet);
        }

        JsonNode scopeNode = jsonNode.get("scope").get(1);
        Set<String>scopes = new HashSet<>();
        if (scopeNode != null && !scopeNode.isEmpty()) {
            for (int i = 0; scopeNode.has(i); i++) {
                scopes.add(scopeNode.get(i).asText());
            }
        }

        return new OAuth2Request(requestParameters, clientId, authorities, approved, scopes, resourceIds, redirectUri, responseTypes, extensionProperties);
    }

    private String getString(JsonNode jsonNode) {
        String resultString = null;
        if (jsonNode != null && !jsonNode.isMissingNode()) {
            resultString = jsonNode.asText();
        }
        return resultString;
    }

    private <K> void getMapFromJsonNode(JsonNode extensionPropertiesNode, Map<String, K> extensionProperties) {
        if (extensionPropertiesNode != null && !extensionPropertiesNode.isEmpty()) {
            Iterator<String> fieldNames = extensionPropertiesNode.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                K value = (K) extensionPropertiesNode.get(key).asText(); //TODO
                extensionProperties.put(key, value);
            }
        }
    }

    private Set<String> getSetFromJsonNode(JsonNode jsonNode) {
        Set<String> resourceIds = new HashSet<>();
        if (jsonNode != null && !jsonNode.isEmpty()) {
            for (int i = 0; jsonNode.has(i); i++) {
                JsonNode resourceIdNode = jsonNode.get(i);
                resourceIds.add(resourceIdNode.asText());
            }
        }
        return resourceIds;
    }
}