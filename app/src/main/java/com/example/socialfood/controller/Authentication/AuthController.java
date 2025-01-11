package com.example.socialfood.controller.Authentication;

import android.content.Context;

import com.example.socialfood.controller.BaseController;
import com.example.socialfood.model.entities.User;
import com.example.socialfood.model.handler.UserHandler;
import com.example.socialfood.utils.UserManager;

/**
 * Controller class for authentication operations. Handles login, registration and user session
 * management.
 */
public class AuthController extends BaseController implements AuthControllerInterface {
    private AuthController authController;
    private final UserHandler userHandler;
    private final UserManager userManager;

    /**
     * Constructs a new AuthController
     * 
     * @param context The application context
     * @param userHandler The user handler to manage user data. If null, a new UserHandler will be created.
     * @param userManager The user manager to manage user sessions. If null, the singleton instance of UserManager will be used.
     */
    public AuthController(Context context, UserHandler userHandler, UserManager userManager) {
        super(context);
        this.userHandler = userHandler != null ? userHandler : new UserHandler(context);
        this.userManager = userManager != null ? userManager : UserManager.getInstance(context);
    }

    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }

    /**
     * Validates and performs login with given credentials
     * 
     * @param username The username to validate
     * @param password The password to validate
     * @return LoginResult indicating the result of the operation
     */
    public com.example.socialfood.controller.Authentication.LoginResult validateAndLogin(String username, String password) {
        if (!validateCredentials(username, password)) {
            return com.example.socialfood.controller.Authentication.LoginResult.EMPTY_FIELDS;
        }

        if (login(username, password)) {
            return com.example.socialfood.controller.Authentication.LoginResult.SUCCESS;
        }

        return com.example.socialfood.controller.Authentication.LoginResult.INVALID_CREDENTIALS;
    }

    /**
     * Validates and performs registration with given credentials
     * 
     * @param username The username to validate
     * @param password The password to validate
     * @return RegisterResult indicating the result of the operation
     */
    public com.example.socialfood.controller.Authentication.RegisterResult validateAndRegister(String username, String password) {
        if (!validateCredentials(username, password)) {
            return com.example.socialfood.controller.Authentication.RegisterResult.EMPTY_FIELDS;
        }

        if (userHandler.getUserByUsername(username) != null) {
            return com.example.socialfood.controller.Authentication.RegisterResult.USERNAME_TAKEN;
        }

        if (register(username, password)) {
            return com.example.socialfood.controller.Authentication.RegisterResult.SUCCESS;
        }

        return com.example.socialfood.controller.Authentication.RegisterResult.USERNAME_TAKEN;
    }

    /**
     * Validates login credentials
     * 
     * @param username The username to validate
     * @param password The password to validate
     * @return true if credentials are valid, false otherwise
     */
    private boolean validateCredentials(String username, String password) {
        return username != null && !username.trim().isEmpty()
                && password != null && !password.isEmpty();
    }

    /**
     * Attempts to login a user with given credentials
     * 
     * @param username The username
     * @param password The password
     * @return true if login successful, false otherwise
     */
    @Override
    public boolean login(String username, String password) {
        User user = userHandler.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            userManager.loginUser(user);
            return true;
        }
        return false;
    }

    /**
     * Registers a new user
     * 
     * @param username The username
     * @param password The password
     * @return true if registration successful, false otherwise
     */
    @Override
    public boolean register(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }

        if (userHandler.getUserByUsername(username) != null) {
            return false;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setProfilImage("default.png");
        newUser.setBio("default bio");
        newUser.setFollowersCount(0);
        newUser.setPostsCount(0);

        return userHandler.insert(newUser);
    }

    /**
     * Logs out the current user
     */
    @Override
    public void logout() {
        userManager.logoutUser();
    }

    /**
     * Checks if a user is currently logged in
     * 
     * @return true if user is logged in, false otherwise
     */
    @Override
    public boolean isLoggedIn() {
        return userManager.isLoggedIn();
    }

    /**
     * Gets the currently logged in user
     * 
     * @return The current User or null if not logged in
     */
    @Override
    public User getCurrentUser() {
        return userManager.getCurrentUser();
    }
}