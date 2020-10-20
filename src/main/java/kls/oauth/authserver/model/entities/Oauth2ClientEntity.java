package kls.oauth.authserver.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "oauth_client_details")
public class Oauth2ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientId;
    @Column(name = "resource_ids")
    private String resourceIds;
    @Column(name = "scope")
    private String scope;
    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;
    @Column(name = "web_server_redirect_uri")
    private String redirectUri;
    @Column(name = "authorities")
    private String authorities;
    @Column(name = "access_token_validity")
    private long accessTokenValidity;
    @Column(name = "refresh_token_validity")
    private long refreshTokenValidity;
    @Column(name = "additional_information")
    private String additionalInformation;
    @Column(name = "autoapprove")
    private boolean autoApproval;

    public Oauth2ClientEntity() {
    }

    public Oauth2ClientEntity(String resourceIds, String scope, String authorizedGrantTypes, String redirectUri, String authorities, long accessTokenValidity, long refreshTokenValidity, String additionalInformation, boolean autoApproval) {
        this.resourceIds = resourceIds;
        this.scope = scope;
        this.authorizedGrantTypes = authorizedGrantTypes;
        this.redirectUri = redirectUri;
        this.authorities = authorities;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.additionalInformation = additionalInformation;
        this.autoApproval = autoApproval;
    }

    public int getClientId() {
        return clientId;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public String getScope() {
        return scope;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getAuthorities() {
        return authorities;
    }

    public long getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public long getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public boolean isAutoApproval() {
        return autoApproval;
    }
}