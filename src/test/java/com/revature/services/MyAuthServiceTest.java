package com.revature.services;

import com.revature.exceptions.NewUserHasNonZeroIdException;
import com.revature.exceptions.NoSuchPassword;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.Role;
import com.revature.repositories.UserDAO;
import com.revature.servlets.UserServlet;

import com.revature.models.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MyAuthServiceTest {
    static AuthService authService;
    @Mock
    static UserDAO userDAO = mock(UserDAO.class);
    static UserService userService;
    private User EMPLOYEE_TO_REGISTER;
    private User GENERIC_EMPLOYEE_1;
    private User GENERIC_FINANCE_MANAGER_1;
    @BeforeClass
    public static void beforeSetup()throws Exception{
        authService=new AuthService();
        userService=mock(UserService.class);

    }

    @Before
    public void runThisBeforeEachTest() throws Exception{
        EMPLOYEE_TO_REGISTER = new User(0, "genericEmployee1", "genericPassword", Role.EMPLOYEE);
        GENERIC_EMPLOYEE_1 = new User(2, "genericEmployee1", "genericPassword", Role.EMPLOYEE);
        GENERIC_FINANCE_MANAGER_1 = new User(2, "genericManager1", "genericPassword", Role.FINANCE_MANAGER);
    }
    @Test
    public void nonUniqueUserNameOnRegisterFailsTest(){
        when(userService.getByUsername(anyString())).thenReturn(Optional.of(GENERIC_EMPLOYEE_1));
        authService.setUserService(userService);
        assertThrows(UsernameNotUniqueException.class,
                () -> authService.register(EMPLOYEE_TO_REGISTER)
        );

        verify(userService, atLeastOnce()).getByUsername(EMPLOYEE_TO_REGISTER.getUsername());
        verify(userService, never()).create(EMPLOYEE_TO_REGISTER);


    }
    @Test
    public void userIsRegisteredIfEverythingCorrect() {
        //Checks if new user is registered successfully
      when(userService.create(anyObject())).thenReturn(GENERIC_EMPLOYEE_1);
      when(userService.getByUsername(anyObject())).thenReturn(Optional.empty());
      authService.setUserService(userService);
      assertEquals(GENERIC_EMPLOYEE_1, authService.register(EMPLOYEE_TO_REGISTER));
      //User u = authService.register(EMPLOYEE_TO_REGISTER);

      verify(userService).getByUsername(EMPLOYEE_TO_REGISTER.getUsername());
      verify(userService).create(EMPLOYEE_TO_REGISTER);

    }
    @Test
    public void userIdOFUnregisteredIsZeroTest(){
        EMPLOYEE_TO_REGISTER.setId(1000);
        assertThrows(NewUserHasNonZeroIdException.class,
                () -> authService.register(EMPLOYEE_TO_REGISTER)
        );
    }
    @Test
    public void loginSucessfulWhenUserInfoMatches(){
        authService.setUserService(userService);
        when(userService.getByUsername(anyString())).thenReturn(Optional.of(GENERIC_EMPLOYEE_1));

        assertEquals(GENERIC_EMPLOYEE_1, authService.login(GENERIC_EMPLOYEE_1.getUsername(), GENERIC_EMPLOYEE_1.getPassword()));

        verify(userService).getByUsername(EMPLOYEE_TO_REGISTER.getUsername());
    }
    @Test
    public void loginFailsWhenUserInfoDoesntMatch(){
        authService.setUserService(userService);
        when(userService.getByUsername(anyString())).thenReturn(Optional.of(GENERIC_EMPLOYEE_1));
        assertThrows(NoSuchPassword.class,
                () -> authService.login(GENERIC_EMPLOYEE_1.getUsername(), "lskjdofso"));
    }





}
