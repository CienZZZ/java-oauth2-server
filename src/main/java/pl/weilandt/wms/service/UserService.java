package pl.weilandt.wms.service;

import io.vavr.collection.List;
import pl.weilandt.wms.dto.NewUserDTO;
import pl.weilandt.wms.dto.UserDTO;

import java.util.Optional;

public interface UserService {

    UserDTO save(NewUserDTO user);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(long id);
    void delete(long id);
    Optional<UserDTO> changePassword(long id, String newPassword);
}
