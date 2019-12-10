package pl.weilandt.wms.user;

import io.vavr.collection.List;

import java.util.Optional;

public interface UserService {

    UserDTO save(NewUserDTO user);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(long id);
    void delete(long id);
    Optional<UserDTO> changePassword(long id, String newPassword);
}
