package kls.oauth.authserver.model.repo;

import kls.oauth.authserver.model.entities.UserMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserMetadata extends JpaRepository<UserMetadata, Integer> {
}
