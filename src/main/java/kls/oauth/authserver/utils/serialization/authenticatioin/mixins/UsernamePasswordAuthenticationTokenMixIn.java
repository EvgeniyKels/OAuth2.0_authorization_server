package kls.oauth.authserver.utils.serialization.authenticatioin.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import kls.oauth.authserver.utils.serialization.authenticatioin.UsernamePasswordAuthenticationTokenDeserializer;

@JsonDeserialize(using = UsernamePasswordAuthenticationTokenDeserializer.class)
public abstract class UsernamePasswordAuthenticationTokenMixIn {

}
