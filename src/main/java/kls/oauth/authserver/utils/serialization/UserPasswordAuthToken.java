package kls.oauth.authserver.utils.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class UserPasswordAuthToken extends JsonDeserializer<UserPasswordAuthToken> {
    @Override
    public UserPasswordAuthToken deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        return null;
    }
}
