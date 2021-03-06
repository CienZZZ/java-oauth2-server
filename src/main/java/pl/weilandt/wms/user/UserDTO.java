package pl.weilandt.wms.user;

import lombok.Getter;
import pl.weilandt.wms.user.role.Role;

import java.time.LocalDate;
import java.util.Set;
import java.util.StringJoiner;

@Getter
public class UserDTO {

    public final long id;
    public final String name;
    public final String password;
    public final LocalDate registerDate;
    public final Boolean active;
    public final Boolean changedPassword;
    public final LocalDate dateLastChange;
    public final Set<Role> roles;

    UserDTO(long id, String name, String password, LocalDate registerDate, Boolean active, Boolean changedPassword, LocalDate dateLastChange, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.registerDate = registerDate;
        this.active = active;
        this.changedPassword = changedPassword;
        this.dateLastChange = dateLastChange;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("password='" + password + "'")
                .add("registerDate=" + registerDate)
                .add("active=" + active)
                .add("changedPassword=" + changedPassword)
                .add("dateLastChange=" + dateLastChange)
                .add("roles=" + roles)
                .toString();
    }
}
