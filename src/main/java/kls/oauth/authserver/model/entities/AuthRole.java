package kls.oauth.authserver.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auth_role")
@Getter @Setter
public class AuthRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String roleName;
    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> user = new ArrayList<>();
}
