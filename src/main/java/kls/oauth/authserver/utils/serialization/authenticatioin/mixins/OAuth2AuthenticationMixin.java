package kls.oauth.authserver.utils.serialization.authenticatioin.mixins;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import kls.oauth.authserver.utils.serialization.authenticatioin.OAuth2AuthenticationDeserializer;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonDeserialize(using = OAuth2AuthenticationDeserializer.class)
public abstract class OAuth2AuthenticationMixin {

}