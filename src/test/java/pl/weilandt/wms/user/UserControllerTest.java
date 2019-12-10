package pl.weilandt.wms.user;

import io.vavr.collection.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.weilandt.wms.exception.ResourceExistsException;
import pl.weilandt.wms.user.role.Role;
import pl.weilandt.wms.user.role.RoleType;
import pl.weilandt.wms.user.role.RoleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

    public Set<Role> getRoleUserToUse(){
        java.util.List<String> roleStr = new ArrayList<>();
        roleStr.add(RoleType.USER.toString());
        Set<Role> role = this.roleRepository.find(roleStr);
        return role;
    }

    @BeforeEach
    public void clearAfterTest(){
        this.userRepository.deleteAll();
    }

    @AfterAll
    public void createAdminUserAfterAllTests(){
        this.userRepository.deleteAll();
        java.util.List<String> roleStr = new ArrayList<>();
        roleStr.add(RoleType.ADMIN.toString());
        roleStr.add(RoleType.USER.toString());
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

    @Test
    public void createUser(){
        final UserDTO created = this.userService.save(new NewUserDTO(
                "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));
        assertNotNull(created);
    }

    @Test
    public void createUserIsReturned(){
        final UserDTO created = this.userService.save(new NewUserDTO(
                "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));
        final List<UserDTO> all = this.userService.getAllUsers();
        assertEquals("Krzys", all.head().name);
    }

    @Test
    public void createdUserHasNewId(){
        final UserDTO created1 = this.userService.save(new NewUserDTO(
                "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));
        final UserDTO created2 = this.userService.save(new NewUserDTO(
                "Adam", "123456", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));
        assertNotEquals(created1.id, created2.id);
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    public void userAlreadyExists(){
        assertThrows(ResourceExistsException.class, ()->{
            final UserDTO created1 = this.userService.save(new NewUserDTO(
                    "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
            ));
            final UserDTO created2 = this.userService.save(new NewUserDTO(
                    "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
            ));
        });
    }

    @Test
    public void userDeleted(){
        final UserDTO created1 = this.userService.save(new NewUserDTO(
                "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));

        UserDTO userToDelete = userService.getUserById(created1.getId());
        this.userService.delete(userToDelete.getId());

        assertFalse(userRepository.findById(created1.getId()).isPresent());
    }
}