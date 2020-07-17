package kls.oauth.authserver.utils.serialization;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import kls.oauth.authserver.utils.serialization.AccessTokenJackson2Deserializer;
import kls.oauth.authserver.utils.serialization.AccessTokenJackson2Serializer;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = AccessTokenJackson2Serializer.class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = AccessTokenJackson2Deserializer.class)
public abstract class AccessTokenMixIn {

}