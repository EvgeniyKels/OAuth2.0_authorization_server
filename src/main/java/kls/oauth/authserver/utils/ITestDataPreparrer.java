package kls.oauth.authserver.utils;

public interface ITestDataPreparrer {
    void insertClient();
    void insertRoles(String[] roles);
    void insertUserAndCreateConnectionForRolesAndClient();
}
