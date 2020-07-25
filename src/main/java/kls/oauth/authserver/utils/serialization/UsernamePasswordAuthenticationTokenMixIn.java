package kls.oauth.authserver.utils.serialization;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = UsernamePasswordAuthenticationTokenDeserializer.class)
public abstract class UsernamePasswordAuthenticationTokenMixIn {

}
