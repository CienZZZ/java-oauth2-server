package pl.weilandt.wms.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.weilandt.wms.model.Role;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
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
