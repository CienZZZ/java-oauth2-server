package pl.weilandt.wms.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pl.weilandt.wms.user.role.Role;

import java.time.LocalDate;
import java.util.Set;

@Getter
public class NewUserDTO {

    public final String name;
    public final String password;
    public final LocalDate registerDate;
    public final Boolean active;
    public final Boolean changedPassword;
    public final LocalDate dateLastChange;
    public final Set<Role> roles;

    @JsonCreator
    public NewUserDTO(
            @JsonProperty("name")
                    String name,
            @JsonProperty("password")
                    String password,
            @JsonProperty("registerDate")
                    LocalDate registerDate,
            @JsonProperty("active")
                    Boolean active,
            @JsonProperty("changedPassword")
                    Boolean changedPassword,
            @JsonProperty("dateLastChange")
                    LocalDate dateLastChange,
            @JsonProperty("roles") Set<Role> roles) {
        this.name = name;
        this.password = password;
        this.registerDate = registerDate;
        this.active = active;
        this.changedPassword = changedPassword;
        this.dateLastChange = dateLastChange;
        this.roles = roles;
    }
}
