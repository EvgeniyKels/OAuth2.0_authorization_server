package kls.oauth.authserver.model.repos;

import kls.oauth.authserver.model.entities.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IRefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findByTokenId(String tokenId);
}