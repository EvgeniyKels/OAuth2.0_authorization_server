package kls.oauth.authserver.model.repos;

import kls.oauth.authserver.model.entities.ClientEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface IClientRepository extends MongoRepository<ClientEntity, ObjectId> {
    @Query("{clientId:?0}")
    ClientEntity receiveClientByClient(String clientId);
}
