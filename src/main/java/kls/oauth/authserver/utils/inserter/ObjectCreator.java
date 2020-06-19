package kls.oauth.authserver.utils.inserter;

import kls.oauth.authserver.model.entities.ClientEntity;
import kls.oauth.authserver.model.entities.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

class ObjectCreator {
    private final PasswordEncoder passwordEncoder;

    ObjectCreator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    List<UserEntity>createUserEntity(int numberOfUsers) {
        String[]roles = {"ROLE_ADMIN", "ROLE_USER", "ROLE_EDITOR"};
        String[]permissions = {"ROLE_PERM_1", "ROLE_PERM_2", "ROLE_PERM_3"};

        List <UserEntity> users = new ArrayList <>();
        for (int i = 0; i < numberOfUsers; i++) {
            users.add(
                    new UserEntity(
                            UUID.randomUUID().toString(),
                            "user_" + i,
                            "email" + i + "@gmail.com",
                            passwordEncoder.encode("password"),
                            Arrays.stream(permissions).map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                            Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
        }

        return users;
    }

    ClientEntity createClient() {
        Set <String> redirectUris = new HashSet <>();
        redirectUris.add("http://localhost:8090/showProtectedResource");

        Set<String>authorizedGrantTypes = new HashSet <>();
        authorizedGrantTypes.add("authorization_code");
        authorizedGrantTypes.add("refresh_token");

        Set<String>resourceIds = new HashSet <>();
        resourceIds.add("resource_server_1");

        String clientSecret = "client_secret";
        return new ClientEntity(
                "1_id",
                resourceIds,
                true,
                passwordEncoder.encode("client_1_secret"),
                false,
                null,
                authorizedGrantTypes,
                redirectUris,
                null,
                10800,
                60000,
                false,
                null
        );
    }
}
