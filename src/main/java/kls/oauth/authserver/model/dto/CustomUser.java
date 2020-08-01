package kls.oauth.authserver.model.dto;

import kls.oauth.authserver.model.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUser extends User {
    private String userId;
    private String name;

    public CustomUser(UserEntity userEntity) {
        super(userEntity.getEmail(), userEntity.getPassword(), prepareGrantedAuthorities(userEntity.getRoles(), userEntity.getPermissions()));
        this.userId = userEntity.getUserId();
        this.name = userEntity.getName();
    }

    private static Collection <? extends GrantedAuthority> prepareGrantedAuthorities(List <String> roles, List <String> permissions) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet <>();
        grantedAuthorities.addAll(roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        grantedAuthorities.addAll(permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
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