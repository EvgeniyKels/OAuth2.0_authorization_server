package kls.oauth.authserver.service;

import kls.oauth.authserver.model.dto.CustomClient;
import kls.oauth.authserver.model.dto.CustomUser;
import kls.oauth.authserver.model.entities.ClientEntity;
import kls.oauth.authserver.model.entities.UserEntity;
import kls.oauth.authserver.model.repos.IClientRepository;
import kls.oauth.authserver.model.repos.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Logger;

@Service
public class CustomAuthDetailsService implements UserDetailsService, ClientDetailsService {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Objects.requireNonNull(userEmail, "user email null");
        UserEntity userEntity = userRepository.reciveUserByEmail(userEmail);
        return new CustomUser(userEntity);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Objects.requireNonNull(clientId, "client id null");
        ClientEntity clientEntity = clientRepository.receiveClientByClient(clientId);
        LOGGER.info(clientEntity.toString());
        return new CustomClient(clientEntity);
    }
}