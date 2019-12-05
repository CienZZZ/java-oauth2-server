package pl.weilandt.wms.controller;


import io.vavr.collection.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.weilandt.wms.dto.NewUserDTO;
import pl.weilandt.wms.dto.UserDTO;
import pl.weilandt.wms.model.Role;
import pl.weilandt.wms.repository.RoleRepository;
import pl.weilandt.wms.repository.UserRepository;
import pl.weilandt.wms.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void clearAfterTest(){
        this.userRepository.deleteAll();
    }

    @AfterAll
    public void createAdminUserAfterAllTests(){
        this.userRepository.deleteAll();
        java.util.List<String> roleStr = new ArrayList<>();
        roleStr.add("ADMIN");
        roleStr.add("USER");
        Set<Role> role = roleRepository.find(roleStr);
        this.userService.save(new NewUserDTO(
                "admin", "admin", LocalDate.now(), true, false, null, role
        ));
    }

    @Test
    public void getEmptyList(){
        final List<UserDTO> users = this.userService.getAllUsers();
        assertTrue(users.isEmpty());
    }


}