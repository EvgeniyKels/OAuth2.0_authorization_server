package kls.oauth.authserver.model.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Document(value = "clients")
public class ClientEntity {
    @Id
    private ObjectId id;
    @Field(name = "client_id")
    private String clientId;
    @Field(name = "resource_id")
    private Set <String> resourceIds;
    @Field(name = "is_secret_required")
    private boolean isSecretRequired;
    @Field(name = "client_secret")
    private String clientSecret;
    @Field(name = "is_scoped")
    private boolean isScoped;
    @Field(name = "scope")
    private Set <String> scope;
    @Field(name = "authorized_grant_types")
    private Set <String> authorizedGrantTypes;
    @Field(name = "registered_redirect_uri")
    private Set <String> registeredRedirectUri;
    @Field(name = "authorities")
    private Collection <GrantedAuthority> authorities;
    @Field(name = "accessTokenValiditySeconds")
    private Integer accessTokenValiditySeconds;
    @Field(name = "refreshTokenValiditySeconds")
    private Integer refreshTokenValiditySeconds;
    @Field(name = "isAutoApprove")
    private boolean isAutoApprove;
    @Field(name = "additionalInformation")
    private Map <String, Object> additionalInformation;

    public ClientEntity() {
    }

    public ClientEntity(String clientId, Set <String> resourceIds, boolean isSecretRequired, String clientSecret, boolean isScoped, Set <String> scope, Set <String> authorizedGrantTypes, Set <String> registeredRedirectUri, Collection <GrantedAuthority> authorities, Integer accessTokenValiditySeconds, Integer refreshTokenValiditySeconds, boolean isAutoApprove, Map <String, Object> additionalInformation) {
        this.clientId = clientId;
        this.resourceIds = resourceIds;
        this.isSecretRequired = isSecretRequired;
        this.clientSecret = clientSecret;
        this.isScoped = isScoped;
        this.scope = scope;
        this.authorizedGrantTypes = authorizedGrantTypes;
        this.registeredRedirectUri = registeredRedirectUri;
        this.authorities = authorities;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.isAutoApprove = isAutoApprove;
        this.additionalInformation = additionalInformation;
    }

    public String getClientId() {
        return clientId;
    }

    public Set <String> getResourceIds() {
        return resourceIds;
    }

    public boolean isSecretRequired() {
        return isSecretRequired;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public boolean isScoped() {
        return isScoped;
    }

    public Set <String> getScope() {
        return scope;
    }

    public Set <String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public Set <String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    public Collection <GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public boolean isAutoApprove() {
        return isAutoApprove;
    }

    public Map <String, Object> getAdditionalInformation() {
        return additionalInformation;
    }
}
