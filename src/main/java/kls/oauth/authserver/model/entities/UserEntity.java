package kls.oauth.authserver.model.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Document(value = "users")
public class UserEntity {
    @Id
    private ObjectId id;
    @Field(name = "user_id")
    private String userId;
    @Field(name = "name")
    private String name;
    @Field(name = "email")
    private String email;
    @Field(name = "password")
    private String password;
    @Field(name = "role")
    private List<String>roles;
    @Field(name = "permission")
    private List<String>permissions;

    public UserEntity(String userId, String name, String email, String password, List <String> roles, List <String> permissions) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
    }

    public UserEntity() {
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List <String> getRoles() {
        return roles;
    }

    public List <String> getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return "{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + " ***** " + '\'' +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }
}