package kls.oauth.authserver.model.repo;

import kls.oauth.authserver.model.entities.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthRoleRepo extends JpaRepository<AuthRole, Integer> {
}
