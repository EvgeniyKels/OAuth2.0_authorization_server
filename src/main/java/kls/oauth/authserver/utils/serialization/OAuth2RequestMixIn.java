package kls.oauth.authserver.utils.serialization;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.oauth2.provider.OAuth2Request;

@JsonDeserialize(using = OAuth2RequestDeserializer.class)
public abstract class OAuth2RequestMixIn {
}
