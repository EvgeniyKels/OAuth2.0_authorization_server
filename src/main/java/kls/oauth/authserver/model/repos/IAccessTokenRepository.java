package kls.oauth.authserver.model.repos;

import kls.oauth.authserver.model.entities.AccessToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IAccessTokenRepository extends MongoRepository<AccessToken, String> {

    List<AccessToken> findByClientId(String clientId);

    List<AccessToken> findByClientIdAndUsername(String clientId, String username);

    Optional<AccessToken> findByTokenId(String tokenId);

    Optional<AccessToken> findByRefreshToken(String refreshToken);

    Optional<AccessToken> findByAuthenticationId(String authenticationId);

}