package kls.oauth.authserver.model.repo;

import kls.oauth.authserver.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepo extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUserLogin(String userLogin);
}
