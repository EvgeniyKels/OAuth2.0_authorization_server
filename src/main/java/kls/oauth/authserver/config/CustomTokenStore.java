package kls.oauth.authserver.config;

import kls.oauth.authserver.model.entities.AccessToken;
import kls.oauth.authserver.model.entities.RefreshToken;
import kls.oauth.authserver.model.repos.IAccessTokenRepository;
import kls.oauth.authserver.model.repos.IRefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomTokenStore implements TokenStore {
    private IAccessTokenRepository accessTokenRepository;
    private IRefreshTokenRepository refreshTokenRepository;
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    public CustomTokenStore(IAccessTokenRepository accessTokenRepository, IRefreshTokenRepository refreshTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        Optional<AccessToken> accessToken = accessTokenRepository.findByTokenId(extractTokenKey(token));
        return accessToken.map(AccessToken::getAuthentication).orElse(null);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String refreshToken = null;
        if (accessToken.getRefreshToken() != null) {
            refreshToken = accessToken.getRefreshToken().getValue();
        }

        if (readAccessToken(accessToken.getValue()) != null) {
            this.removeAccessToken(accessToken);
        }

        AccessToken newAccessToken = new AccessToken(
                UUID.randomUUID().toString(),
                extractTokenKey(accessToken.getValue()),
                accessToken,
                authenticationKeyGenerator.extractKey(authentication),
                authentication.isClientOnly() ? null : authentication.getName(),
                authentication.getOAuth2Request().getClientId(),
                authentication,
                extractTokenKey(refreshToken)
        );

        accessTokenRepository.save(newAccessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        Optional<AccessToken> accessToken = accessTokenRepository.findByTokenId(extractTokenKey(tokenValue));
        return accessToken.map(AccessToken::getToken).orElse(null);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        Optional<AccessToken> accessToken = accessTokenRepository.findByTokenId(extractTokenKey(token.getValue()));
        accessToken.ifPresent(value -> accessTokenRepository.delete(value));
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        RefreshToken newRefreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                extractTokenKey(refreshToken.getValue()),
                refreshToken,
                authentication
        );
        refreshTokenRepository.save(newRefreshToken);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByTokenId(extractTokenKey(tokenValue));
        refreshToken.ifPresent(RefreshToken::getToken);
        return null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByTokenId(extractTokenKey(token.getValue()));
        return refreshToken.map(RefreshToken::getAuthentication).orElse(null);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByTokenId(extractTokenKey(token.getValue()));
        refreshToken.ifPresent(value -> refreshTokenRepository.delete(value));
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        Optional<AccessToken> token = accessTokenRepository.findByRefreshToken(extractTokenKey(refreshToken.getValue()));
        token.ifPresent(accessToken -> accessTokenRepository.delete(accessToken));
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = null;
        String authenticationId = authenticationKeyGenerator.extractKey(authentication);
        Optional<AccessToken> token = accessTokenRepository.findByAuthenticationId(authenticationId);

        if(token.isPresent()) {
            accessToken = token.get().getToken();
            if(accessToken != null && !authenticationId.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken)))) {
                this.removeAccessToken(accessToken);
                this.storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List<AccessToken> result = accessTokenRepository.findByClientIdAndUsername(clientId, userName);
        return result.stream().map(AccessToken::getToken).collect(Collectors.toList());
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<AccessToken> result = accessTokenRepository.findByClientId(clientId);
        return result.stream().map(AccessToken::getToken).collect(Collectors.toList());
    }

    private String extractTokenKey(String value) {
        if(value == null) {
            return null;
        } else {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var5) {
                throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
            }

            try {
                byte[] e = digest.digest(value.getBytes("UTF-8"));
                return String.format("%032x", new Object[]{new BigInteger(1, e)});
            } catch (UnsupportedEncodingException var4) {
                throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
            }
        }
    }
}
