package kls.oauth.authserver.utils.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
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
        Map<String, String> requestParameters = new LinkedHashMap<>();
        JsonNode requestParametersNode = jsonNode.get("requestParameters");
        if (requestParametersNode != null && !requestParametersNode.isEmpty()) {
            Iterator<String> fieldNamesIt = requestParametersNode.fieldNames();
            while (fieldNamesIt.hasNext()) {
                String fieldName = fieldNamesIt.next();
                String value = requestParametersNode.get(fieldName).asText();
                requestParameters.put(fieldName, value);
            }
        }

        String clientId = null;
        JsonNode clientIdNode = jsonNode.get("clientId");
        if (clientIdNode != null && !clientIdNode.isMissingNode()) {
            clientId = clientIdNode.asText();
        }

        JsonNode approvedNode = jsonNode.get("approved");
        boolean approved = false;
        if (approvedNode != null && !approvedNode.isMissingNode()) {
            approved = approvedNode.asBoolean();
        }

        JsonNode scopesNode = jsonNode.get("scope").get(1);
        Set<String>scopes = new HashSet<>();
        if (scopesNode != null && !scopesNode.isEmpty()) {
            for (int i = 0; scopesNode.has(i); i++) {
                JsonNode scopeNode = scopesNode.get(i);
                scopes.add(scopeNode.asText());
            }
        }

        JsonNode resourceIdsNode = jsonNode.get("resourceIds").get(1);
        Set<String>resourceIds = new HashSet<>();
        if (resourceIdsNode != null && !resourceIdsNode.isEmpty()) {
            for (int i = 0; resourceIdsNode.has(i); i++) {
                JsonNode resourceIdNode = resourceIdsNode.get(i);
                resourceIds.add(resourceIdNode.asText());
            }
        }

        JsonNode redirectUriNode = jsonNode.get("redirectUri");
        String redirectUri = null;
        if (redirectUriNode != null && !redirectUriNode.isMissingNode()) {
            redirectUri = redirectUriNode.asText();
        }

        JsonNode responseTypesNode = jsonNode.get("responseTypes").get(1);
        Set<String>responseTypes = new HashSet<>();
        if (responseTypesNode != null && !responseTypesNode.isEmpty()) {
            for (int i = 0; responseTypesNode.has(i); i++) {
                JsonNode responseTypeNode = responseTypesNode.get(i);
                responseTypes.add(responseTypeNode.asText());
            }
        }

        JsonNode extensionPropertiesNode = jsonNode.get("extensions").get(1);
        Map<String, Serializable> extensionProperties = new HashMap<>();
        if (extensionPropertiesNode != null && !extensionPropertiesNode.isEmpty()) {
            Iterator<String> fieldNames = extensionPropertiesNode.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                String value = extensionPropertiesNode.get(key).asText();
                extensionProperties.put(key, value);
            }
        }

        JsonNode authoritiesNode = jsonNode.get("authorities").get(1);
        Collection<? extends GrantedAuthority> authorities = new HashSet<>();
        if (authoritiesNode != null && !authoritiesNode.isEmpty()) {
            for (int i = 0; authoritiesNode.has(i); i++) {
                JsonNode authorityNode = authoritiesNode.get(i);
                String authority = approvedNode.asText();
                //TODO ?????????????????????????????????????????
            }
        }

        return new OAuth2Request(requestParameters, clientId, authorities, approved, scopes, resourceIds, redirectUri, responseTypes, extensionProperties);
    }
}
//    Map<String, String> requestParameters, String clientId,
//    boolean approved, Set<String> scope,
//    java.util.Set<String> resourceIds, String redirectUri, Set<String> responseTypes,
//    Map<String, Serializable> extensionProperties, Collection<? extends GrantedAuthority> authorities,
