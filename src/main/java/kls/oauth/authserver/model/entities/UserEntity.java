package kls.oauth.authserver.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter @Setter @NoArgsConstructor
public class UserEntity {
    @Id
    @GenericGenerator(name = "user_id", strategy = "kls.oauth.authserver.utils.model_utils.ClientIdGenerator")
    @GeneratedValue(generator = "user_id")
    public String userId;
    public String userLogin;
    public String userEmail;
    public String userPassword;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    public List<AuthRole> roles = new ArrayList<>();
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    public UserMetadata userMetadatara;

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " " +
                userLogin +
                " " +
                userEmail;
    }
}
