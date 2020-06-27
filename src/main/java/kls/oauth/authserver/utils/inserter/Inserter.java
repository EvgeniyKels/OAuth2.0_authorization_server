package kls.oauth.authserver.utils.inserter;

import kls.oauth.authserver.model.repos.IClientRepository;
import kls.oauth.authserver.model.repos.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Inserter {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void insertTestData(int numberOfUsers) {
        userRepository.deleteAll();
        clientRepository.deleteAll();

        ObjectCreator objectCreator = new ObjectCreator(passwordEncoder);
        userRepository.saveAll(objectCreator.createUserEntity(numberOfUsers));
        clientRepository.save(objectCreator.createClientWithAuthCode());
        clientRepository.save(objectCreator.createClientWithPasswordOwner());
    }
}
