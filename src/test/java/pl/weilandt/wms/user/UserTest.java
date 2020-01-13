package pl.weilandt.wms.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UserTest {

    private User userMock = mock(User.class);

    @Test
    void constructorTest(){
        String expectedUserOneName = "Bambo";
        String expectedUserOnePassword = "12345";
        String expectedUserTwoName = "Adam";
        String expectedUserTwoPassword = "wsx321";

        User user = new User(expectedUserOneName, expectedUserOnePassword, LocalDate.now(), true, false, null, null);
        assertThat(user.getName()).isEqualTo(expectedUserOneName);
        assertThat(user.getPassword()).isEqualTo(expectedUserOnePassword);

        user = new User(expectedUserTwoName, expectedUserTwoPassword, LocalDate.now(), true, false, null, null);
        assertThat(user.getName()).isEqualTo(expectedUserTwoName);
        assertThat(user.getPassword()).isEqualTo(expectedUserTwoPassword);
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