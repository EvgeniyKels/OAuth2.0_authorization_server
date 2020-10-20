package kls.oauth.authserver.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_metadata")
@Getter @Setter
public class UserMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String familyName;
    private String mobilePhone;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
