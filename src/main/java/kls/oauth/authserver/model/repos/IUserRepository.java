package kls.oauth.authserver.model.repos;

import kls.oauth.authserver.model.entities.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface IUserRepository extends MongoRepository<UserEntity, ObjectId> {
    @Query("{email:?0}")
    UserEntity reciveUserByEmail(String userEmail);
}
