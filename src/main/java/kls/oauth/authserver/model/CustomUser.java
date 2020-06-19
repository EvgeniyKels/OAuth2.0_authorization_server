package kls.oauth.authserver.model;

import kls.oauth.authserver.model.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CustomUser extends User {
    private String userId;
    private String name;

    public CustomUser(UserEntity userEntity) {
        super(userEntity.getEmail(), userEntity.getPassword(), prepareGrantedAuthorities(userEntity.getRoles(), userEntity.getPermissions()));
        this.userId = userEntity.getUserId();
        this.name = userEntity.getName();
    }

    private static Collection <? extends GrantedAuthority> prepareGrantedAuthorities(List <GrantedAuthority> roles, List <GrantedAuthority> permissions) {
        HashSet <GrantedAuthority> grantedAuthorities = new HashSet <>();
        grantedAuthorities.addAll(roles);
        grantedAuthorities.addAll(permissions);
        return grantedAuthorities;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}