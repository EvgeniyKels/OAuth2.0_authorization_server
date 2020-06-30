package kls.oauth.authserver.model.entities;

import kls.oauth.authserver.utils.SerializableObjectConverter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Document("refresh_tokens")
public class RefreshToken {
    @Id
    private String id;
    private String tokenId;
    private OAuth2RefreshToken token;
    private String authentication;

    public RefreshToken() {
    }

    public RefreshToken(String id, String tokenId, OAuth2RefreshToken token, OAuth2Authentication authentication) {
        this.id = id;
        this.tokenId = tokenId;
        this.token = token;
        this.authentication = SerializableObjectConverter.serialize(authentication);
    }

    public String getId() {
        return id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public OAuth2RefreshToken getToken() {
        return token;
    }

    public OAuth2Authentication getAuthentication() {
        return SerializableObjectConverter.deserialize(authentication);
    }

    public void setAuthentication(OAuth2Authentication authentication) {
        this.authentication = SerializableObjectConverter.serialize(authentication);
    }
}
