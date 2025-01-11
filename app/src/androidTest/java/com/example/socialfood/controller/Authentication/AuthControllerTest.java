package com.example.socialfood.controller.Authentication;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.socialfood.model.entities.User;
import com.example.socialfood.model.handler.UserHandler;
import com.example.socialfood.utils.UserManager;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AuthController. Tests login and registration functionality.
 */
public class AuthControllerTest {

    private com.example.socialfood.controller.Authentication.AuthController authController;
    private UserHandler mockUserHandler;
    private UserManager mockUserManager;

    /**
     * Sets up the test environment before each test. Initializes mocks and creates AuthController
     * instance.
     */
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        mockUserHandler = mock(UserHandler.class);
        mockUserManager = mock(UserManager.class);

        authController = new com.example.socialfood.controller.Authentication.AuthController(context, mockUserHandler, mockUserManager);
        authController.setAuthController(authController);

    }

    /**
     * Tests successful login with valid credentials. Verifies that: - User is retrieved from
     * UserHandler - Login is successful - UserManager.loginUser is called
     */
    @Test
    public void testValidateAndLogin_Success() {
        String username = "testUser";
        String password = "testPass";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(password);

        when(mockUserHandler.getUserByUsername(username)).thenReturn(mockUser);

        com.example.socialfood.controller.Authentication.LoginResult result = authController.validateAndLogin(username, password);
        assertEquals(com.example.socialfood.controller.Authentication.LoginResult.SUCCESS, result);

        verify(mockUserManager).loginUser(mockUser);
    }

    /**
     * Tests login failure scenario with invalid credentials.
     */
    @Test
    public void testValidateAndLogin_InvalidCredentials() {
        String username = "testUser";
        String password = "wrongPass";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword("correctPass");

        when(mockUserHandler.getUserByUsername(username)).thenReturn(mockUser);

        com.example.socialfood.controller.Authentication.LoginResult result = authController.validateAndLogin(username, password);
        assertEquals(com.example.socialfood.controller.Authentication.LoginResult.INVALID_CREDENTIALS, result);

        verify(mockUserManager, never()).loginUser(mockUser);
    }

    @Test
    public void testValidateAndRegister_Success() {
        String username = "newUser";
        String password = "newPass";

        when(mockUserHandler.getUserByUsername(username)).thenReturn(null);
        when(mockUserHandler.insert(any(User.class))).thenReturn(true);

        com.example.socialfood.controller.Authentication.RegisterResult result = authController.validateAndRegister(username, password);
        assertEquals(com.example.socialfood.controller.Authentication.RegisterResult.SUCCESS, result);
    }

    /**
     * Tests successful registration scenario with valid credentials.
     */
    @Test
    public void testValidateAndRegister_UsernameTaken() {
        String username = "existingUser";
        String password = "somePass";

        User mockUser = new User();
        mockUser.setUsername(username);

        when(mockUserHandler.getUserByUsername(username)).thenReturn(mockUser);

        com.example.socialfood.controller.Authentication.RegisterResult result = authController.validateAndRegister(username, password);
        assertEquals(com.example.socialfood.controller.Authentication.RegisterResult.USERNAME_TAKEN, result);
    }

    /**
     * Tests the logout functionality.
     */
    @Test
    public void testLogout() {
        authController.logout();
        verify(mockUserManager).logoutUser();
    }

    /**
     * Tests the session management functionality.
     */
    @Test
    public void testIsLoggedIn() {
        when(mockUserManager.isLoggedIn()).thenReturn(true);
        assertTrue(authController.isLoggedIn());

        when(mockUserManager.isLoggedIn()).thenReturn(false);
        assertFalse(authController.isLoggedIn());
    }

    /**
     * Tests retrieving the currently logged-in user.
     */
    @Test
    public void testGetCurrentUser() {
        User mockUser = new User();
        when(mockUserManager.getCurrentUser()).thenReturn(mockUser);

        User currentUser = authController.getCurrentUser();
        assertEquals(mockUser, currentUser);
    }
}
