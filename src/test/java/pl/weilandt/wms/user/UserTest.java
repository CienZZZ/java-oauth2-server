package pl.weilandt.wms.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UserTest {

    private User userMock = mock(User.class);

    @Test
    void constructorTest(){
        User user = new User("Bambo", "12345", LocalDate.now(), true, false, null, null);
        assertEquals("Bambo", user.getName());
        assertEquals("12345", user.getPassword());
        user = new User("Adam", "wsx321", LocalDate.now(), true, false, null, null);
        assertEquals("Adam", user.getName());
        assertEquals("wsx321", user.getPassword());
    }

    @Test
    void testIfUserIsAUser(){
        assertTrue(userMock instanceof User);
    }

    @Test
    void userHasRole(){
        assertNotNull(userMock.getRoles());
    }
}