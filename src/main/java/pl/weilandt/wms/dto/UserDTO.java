package pl.weilandt.wms.dto;

import lombok.Getter;
import pl.weilandt.wms.model.Role;

import java.time.LocalDate;
import java.util.Set;

@Getter
//@Setter
public class UserDTO {

    public final long id;
    public final String name;
    public final String password;
    public final LocalDate registerDate;
    public final Boolean active;
    public final Boolean changedPassword;
    public final LocalDate dateLastChange;
    public final Set<Role> roles;

    public UserDTO(long id, String name, String password, LocalDate registerDate, Boolean active, Boolean changedPassword, LocalDate dateLastChange, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.registerDate = registerDate;
        this.active = active;
        this.changedPassword = changedPassword;
        this.dateLastChange = dateLastChange;
        this.roles = roles;
    }

    //    public List<String> getRole() {
//        return roles;
//    }
//
//    public void setRole(List<String> role) {
//        this.roles = roles;
//    }
}
