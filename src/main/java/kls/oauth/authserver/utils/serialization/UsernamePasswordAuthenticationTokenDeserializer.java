package kls.oauth.authserver.utils.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import kls.oauth.authserver.model.dto.CustomUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class UsernamePasswordAuthenticationTokenDeserializer extends JsonDeserializer<UsernamePasswordAuthenticationToken> {

    @Override
    public UsernamePasswordAuthenticationToken deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return deser(p, ctxt, typeDeserializer);
    }

    private UsernamePasswordAuthenticationToken deser(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        ObjectMapper om = (ObjectMapper)p.getCodec();
        JsonNode jsonNode = om.readTree(p);
        System.out.println(jsonNode);
//        UsernamePasswordAuthenticationToken(Object principal, Object credentials)
//        UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority > authorities)
        JsonNode principalNode = jsonNode.get("principal");
        Object principal = null;
        if (principalNode != null) {
            principal = om.readValue(principalNode.toString(), CustomUser.class);
        }

        JsonNode authoritiesNode = jsonNode.get("authorities").get(1);
        System.out.println(authoritiesNode);
        Collection<GrantedAuthority>grantedAuthorities = new HashSet<>();
        if (authoritiesNode != null && !authoritiesNode.isEmpty()) {
            for (int i = 0; authoritiesNode.has(i); i++) {
                JsonNode authorityNode = authoritiesNode.get(i).get("authority");
                String role = authorityNode.asText();
                grantedAuthorities.add(new SimpleGrantedAuthority(role));
            }
        }

        JsonNode authenticatedNode = jsonNode.get("authenticated");
        boolean authenticated = false;
        if (authenticatedNode != null && !authenticatedNode.isMissingNode()) {
            authenticated = authenticatedNode.asBoolean();
        }
        UsernamePasswordAuthenticationToken authenticationToken = null;
        if (authenticated) {
            authenticationToken = new UsernamePasswordAuthenticationToken(principal, jsonNode.get("credentials"), grantedAuthorities);
        } else {
            authenticationToken = new UsernamePasswordAuthenticationToken(principal, jsonNode.get("credentials"));
        }
        return authenticationToken;
    }

    @Override
    public UsernamePasswordAuthenticationToken deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return deser(p, ctxt, null);
    }
}
