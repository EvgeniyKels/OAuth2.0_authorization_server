package kls.oauth.authserver.model.repo;

import kls.oauth.authserver.model.entities.Oauth2ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOauth2ClientRepo extends JpaRepository<Oauth2ClientEntity, Integer> {
}
