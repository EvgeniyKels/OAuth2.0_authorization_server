package kls.oauth.authserver.utils;

import kls.oauth.authserver.model.entities.Oauth2ClientEntity;
import kls.oauth.authserver.model.repo.IOauth2ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestDataPreparerImpl implements ITestDataPreparrer {

    private IOauth2ClientRepo clientRepo;

    @Autowired
    public TestDataPreparerImpl(IOauth2ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public void insertClient() {
        clientRepo.save(new Oauth2ClientEntity());
    }

    @Override
    public void insertRoles(String[] roles) {

    }

    @Override
    public void insertUserAndCreateConnectionForRolesAndClient() {

    }
}
