package kls.oauth.authserver.utils.serialization.authenticatioin.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import kls.oauth.authserver.utils.serialization.authenticatioin.OAuth2RequestDeserializer;

@JsonDeserialize(using = OAuth2RequestDeserializer.class)
public abstract class OAuth2RequestMixIn {
}
