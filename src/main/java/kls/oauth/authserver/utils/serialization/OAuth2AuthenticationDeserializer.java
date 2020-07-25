package kls.oauth.authserver.utils.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.io.IOException;

public class OAuth2AuthenticationDeserializer extends JsonDeserializer<OAuth2Authentication> {
    @Override
    public OAuth2Authentication deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode jsonNode = jp.getCodec().readTree(jp);
        JsonNode userAuthentication = jsonNode.get("userAuthentication");
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.addMixIn(UsernamePasswordAuthenticationToken.class, UsernamePasswordAuthenticationTokenMixIn.class);
        om.addMixIn(OAuth2Request.class, OAuth2RequestMixIn.class);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(UsernamePasswordAuthenticationToken.class, new UsernamePasswordAuthenticationTokenDeserializer());
        om.registerModule(simpleModule);

        SimpleModule simpleModule1 = new SimpleModule();
        simpleModule1.addDeserializer(OAuth2Request.class, new OAuth2RequestDeserializer());
        om.registerModule(simpleModule1);

        Authentication authentication = null;
        if (userAuthentication != null && !userAuthentication.isMissingNode()) {
            authentication = om.readValue(userAuthentication.traverse(jp.getCodec()), UsernamePasswordAuthenticationToken.class);
        }

        JsonNode storedRequest = jsonNode.get("storedRequest");
        OAuth2Request oAuth2Request = om.readValue(storedRequest.traverse(jp.getCodec()), OAuth2Request.class);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        JsonNode details = jsonNode.get("details");
        if(details != null && !details.isMissingNode()) {
            oAuth2Authentication.setDetails(om.readValue(details.traverse(), OAuth2AuthenticationDetails.class));
        }

        return oAuth2Authentication;
    }
}
