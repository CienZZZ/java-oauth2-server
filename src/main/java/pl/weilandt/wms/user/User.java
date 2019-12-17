package pl.weilandt.wms.user;

import lombok.Getter;
import lombok.Setter;
import pl.weilandt.wms.user.role.Role;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column( name="id", nullable=false )
    private Long id;

    @Column( name="name", nullable=false, length=255 )
    private String name;

    @Column( name="password", nullable=false, length=255)
    private String password;

    @Column( name="registerDate" )
    private LocalDate registerDate;

    @Column( name="active" )
    private Boolean active;

    @Column( name="changedPassword" )
    private Boolean changedPassword;

    @Column( name="dateLastChange" )
    private LocalDate dateLastChange;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "User_ROLES",
            joinColumns =  @JoinColumn(name ="USER_ID"),inverseJoinColumns= @JoinColumn(name="ROLE_ID"))
    private Set<Role> roles;

    User(String name, String password, LocalDate registerDate, Boolean active, Boolean changedPassword, LocalDate dateLastChange, Set<Role> roles) {
        this.name = name;
        this.password = password;
        this.registerDate = registerDate;
        this.active = active;
        this.changedPassword = changedPassword;
        this.dateLastChange = dateLastChange;
        this.roles = roles;
    }

    protected User() {
    }

    UserDTO toUserDTO(){
        return new UserDTO(
                this.getId(),
                this.getName(),
                this.getPassword(),
                this.getRegisterDate(),
                this.getActive(),
                this.getChangedPassword(),
                this.getDateLastChange(),
                this.getRoles()
        );
    }
}
