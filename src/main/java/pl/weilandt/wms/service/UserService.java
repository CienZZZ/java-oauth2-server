package pl.weilandt.wms.service;

import io.vavr.collection.List;
import pl.weilandt.wms.dto.UserDTO;

public interface UserService {

    UserDTO save(UserDTO user);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(long id);
    void delete(long id);
}
