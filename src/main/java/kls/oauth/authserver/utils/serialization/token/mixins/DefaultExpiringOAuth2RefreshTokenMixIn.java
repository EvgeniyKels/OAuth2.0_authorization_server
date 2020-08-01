package kls.oauth.authserver.utils.serialization.token.mixins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public abstract class DefaultExpiringOAuth2RefreshTokenMixIn {
    @JsonCreator
    public DefaultExpiringOAuth2RefreshTokenMixIn(
            @JsonProperty("value") String value,
            @JsonProperty("expiration") Date expiration
            ) {
    }
}
