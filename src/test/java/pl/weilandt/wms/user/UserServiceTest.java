package pl.weilandt.wms.user;

import io.vavr.collection.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.weilandt.wms.exception.NoUserException;
import pl.weilandt.wms.exception.ResourceExistsException;
import pl.weilandt.wms.exception.ResourceNotFoundException;
import pl.weilandt.wms.user.role.Role;
import pl.weilandt.wms.user.role.RoleRepository;
import pl.weilandt.wms.user.role.RoleType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    Set<Role> getRoleUserToUse(){
        java.util.List<String> roleStr = new ArrayList<>();
        roleStr.add(RoleType.USER.toString());
        Set<Role> role = this.roleRepository.find(roleStr);
        return role;
    }

    @BeforeEach
    void clearBeforeTest(){
        this.userRepository.deleteAll();
    }

    @AfterAll
    void clearAfterAllTests(){
        this.userRepository.deleteAll();
    }

    @Test
    void getEmptyList(){
        final List<UserDTO> users = this.userService.getAllUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    void createUser(){
        final UserDTO created = this.userService.createNew(new NewUserDTO(
                "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));
        assertNotNull(created);
    }

    @Test
    void createUserIsReturned(){
        final UserDTO created = this.userService.createNew(new NewUserDTO(
                "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));
        final List<UserDTO> all = this.userService.getAllUsers();
        assertEquals("Krzys", all.head().name);
    }

    @Test
    void createdUserHasNewId(){
        final UserDTO created1 = this.userService.createNew(new NewUserDTO(
                "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));
        final UserDTO created2 = this.userService.createNew(new NewUserDTO(
                "Adam", "123456", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));
        assertNotEquals(created1.id, created2.id);
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void userAlreadyExists(){
        assertThrows(ResourceExistsException.class, ()->{
            final UserDTO created1 = this.userService.createNew(new NewUserDTO(
                    "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
            ));
            final UserDTO created2 = this.userService.createNew(new NewUserDTO(
                    "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
            ));
        });
    }

    @Test
    void userDeleted(){
        final UserDTO created1 = this.userService.createNew(new NewUserDTO(
                "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));

        UserDTO userToDelete = userService.getUserById(created1.getId());
        this.userService.delete(userToDelete.getId());

        assertFalse(userRepository.findById(created1.getId()).isPresent());
    }

    @Test
    void rightUserLoaded(){
        final UserDTO created1 = this.userService.createNew(new NewUserDTO(
                "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));
        final UserDTO created2 = this.userService.createNew(new NewUserDTO(
                "Adam", "123456", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));

        assertEquals("Krzys",userService.loadUserByUsername("Krzys").getUsername());
    }

    @Test
    void userHasRightRole(){
        final UserDTO created1 = this.userService.createNew(new NewUserDTO(
                "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));

        assertEquals(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), userService.loadUserByUsername("Krzys").getAuthorities());
    }

    @Test
    void getUserByName(){
        final UserDTO created = this.userService.createNew(new NewUserDTO(
                "Krzys", "admin123", LocalDate.now(), true, false, null, getRoleUserToUse()
        ));
        assertEquals("Krzys", userService.getUserByName(created.getName()).getName());
    }

    @Test
    void userNotFoundByName(){
        assertThrows(UsernameNotFoundException.class, ()->{
            this.userService.loadUserByUsername("Zuzia");
        });
    }

    @Test
    void userNotFoundById(){
        assertThrows(NoUserException.class, ()->{
           this.userService.getUserById(20939L);
        });
    }

    @Test
    void userNotFoundToDelete(){
        assertThrows(ResourceNotFoundException.class, ()->{
           this.userService.delete(204299L);
        });
    }
}