package kls.oauth.authserver.model.dto;

import kls.oauth.authserver.model.entities.ClientEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CustomClient implements ClientDetails {
    private String clientId;
    private Set <String> resourceIds;
    private boolean isSecretRequired;
    private String clientSecret;
    private boolean isScoped;
    private Set <String> scope;
    private Set <String> authorizedGrantTypes;
    private Set <String> registeredRedirectUri;
    private Collection <GrantedAuthority> authorities;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private boolean isAutoApprove;
    private Map <String, Object> additionalInformation;

    public CustomClient() {
    }

    public CustomClient(ClientEntity clientEntity) {
        this.clientId = clientEntity.getClientId();
        this.resourceIds = clientEntity.getResourceIds();
        this.isSecretRequired = clientEntity.isSecretRequired();
        this.clientSecret = clientEntity.getClientSecret();
        this.isScoped = clientEntity.isScoped();
        this.scope = clientEntity.getScope();
        this.authorizedGrantTypes = clientEntity.getAuthorizedGrantTypes();
        this.registeredRedirectUri = clientEntity.getRegisteredRedirectUri();
        this.authorities = clientEntity.getAuthorities();
        this.accessTokenValiditySeconds = clientEntity.getAccessTokenValiditySeconds();
        this.refreshTokenValiditySeconds = clientEntity.getRefreshTokenValiditySeconds();
        this.isAutoApprove = clientEntity.isAutoApprove();
        this.additionalInformation = clientEntity.getAdditionalInformation();
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public Set <String> getResourceIds() {
        return this.resourceIds;
    }

    @Override
    public boolean isSecretRequired() {
        return this.isSecretRequired;
    }

    @Override
    public String getClientSecret() {
        return this.clientSecret;
    }

    @Override
    public boolean isScoped() {
        return this.isScoped;
    }

    @Override
    public Set <String> getScope() {
        return this.scope;
    }

    @Override
    public Set <String> getAuthorizedGrantTypes() {
        return this.authorizedGrantTypes;
    }

    @Override
    public Set <String> getRegisteredRedirectUri() {
        return this.registeredRedirectUri;
    }

    @Override
    public Collection <GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return this.isAutoApprove;
    }

    @Override
    public Map <String, Object> getAdditionalInformation() {
        return this.additionalInformation;
    }
}