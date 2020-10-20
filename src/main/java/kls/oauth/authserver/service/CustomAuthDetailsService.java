package kls.oauth.authserver.service;

import kls.oauth.authserver.model.entities.UserEntity;
import kls.oauth.authserver.model.repo.IOauth2ClientRepo;
import kls.oauth.authserver.model.repo.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CustomAuthDetailsService implements UserDetailsService, ClientDetailsService {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());
    private IUserRepo userRepo;
    private IOauth2ClientRepo clientRepo;
    @Autowired
    public CustomAuthDetailsService(IUserRepo userRepo, IOauth2ClientRepo clientRepo) {
        this.userRepo = userRepo;
        this.clientRepo = clientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        Optional<UserEntity> byUserLoginOpt = userRepo.findByUserLogin(userLogin);
        if (byUserLoginOpt.isEmpty()) {
            throw new UsernameNotFoundException("user with login did not not present in db");
        }

        return null;
    }

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        return null;
    }
}