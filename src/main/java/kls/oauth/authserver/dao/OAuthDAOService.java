package kls.oauth.authserver.dao;

import kls.oauth.authserver.model.UserEntity;

public interface OAuthDAOService {
    public UserEntity getUserDetails(String emailId);
}