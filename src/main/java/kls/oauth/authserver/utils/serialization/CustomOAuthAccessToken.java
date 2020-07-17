package kls.oauth.authserver.utils.serialization;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class CustomOAuthAccessToken implements OAuth2AccessToken {
    public Map<String, Object> additionalInformation;
    public Set<String> scope;
    public OAuth2RefreshToken refreshToken;
    public String tokenType;
    public boolean expired;
    public Date expiration;
    public int expiresIn;
    public String value;

    public CustomOAuthAccessToken(Map<String, Object> additionalInformation,
                                  Set<String> scope,
                                  OAuth2RefreshToken refreshToken,
                                  String tokenType,
                                  boolean expired,
                                  Date expiration,
                                  int expiresIn,
                                  String value) {
        this.additionalInformation = additionalInformation;
        this.scope = scope;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expired = expired;
        this.expiration = expiration;
        this.expiresIn = expiresIn;
        this.value = value;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    @Override
    public Set<String> getScope() {
        return scope;
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String getTokenType() {
        return tokenType;
    }

    @Override
    public boolean isExpired() {
        return expired;
    }

    @Override
    public Date getExpiration() {
        return expiration;
    }

    @Override
    public int getExpiresIn() {
        return expiresIn;
    }

    @Override
    public String getValue() {
        return value;
    }
}
