package pl.weilandt.wms.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UserTest {

    private User userMock = mock(User.class);

    @Test
    void constructorTest(){
        User user = new User("Bambo", "12345", LocalDate.now(), true, false, null, null);
        assertThat(user.getName()).isEqualTo("Bambo");
        assertThat(user.getPassword()).isEqualTo("12345");
        user = new User("Adam", "wsx321", LocalDate.now(), true, false, null, null);
        assertThat(user.getName()).isEqualTo("Adam");
        assertThat(user.getPassword()).isEqualTo("wsx321");
    }

    @Test
    void testIfUserIsAUser(){
        assertThat(userMock).isInstanceOf(User.class);
    }

    @Test
    void userGetRoleNotNull(){
        assertThat(userMock.getRoles()).isNotNull();
    }
}