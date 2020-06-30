package kls.oauth.authserver.model.entities;

import kls.oauth.authserver.utils.SerializableObjectConverter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Document(value = "access_tokens")
public class AccessToken {
    @Id
    private String id;
    private String tokenId;
    private OAuth2AccessToken token;
    private String authenticationId;
    private String username;
    private String clientId;
    private String authentication;
    private String refreshToken;

    public AccessToken() {
    }

    public AccessToken(String id, String tokenId, OAuth2AccessToken token, String authenticationId, String username, String clientId, OAuth2Authentication authentication, String refreshToken) {
        this.id = id;
        this.tokenId = tokenId;
        this.token = token;
        this.authenticationId = authenticationId;
        this.username = username;
        this.clientId = clientId;
        this.authentication = SerializableObjectConverter.serialize(authentication);
        this.refreshToken = refreshToken;
    }

    public OAuth2Authentication getAuthentication() {
        return SerializableObjectConverter.deserialize(authentication);
    }

    public void setAuthentication(OAuth2Authentication authentication) {
        this.authentication = SerializableObjectConverter.serialize(authentication);
    }

    public String getId() {
        return id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public OAuth2AccessToken getToken() {
        return token;
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public String getUsername() {
        return username;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}