package com.example.socialfood.controller.Authentication;

import com.example.socialfood.model.entities.User;

/**
 * Interface defining authentication operations for the social food application. Provides methods
 * for user authentication, registration and session management.
 */
public interface AuthControllerInterface {

    /**
     * Authenticates a user with the provided credentials
     * 
     * @param username The username of the user
     * @param password The password of the user
     * @return true if login was successful, false otherwise
     */
    boolean login(String username, String password);

    /**
     * Registers a new user with the provided credentials
     * 
     * @param username The username for the new user
     * @param password The password for the new user
     * @return true if registration was successful, false otherwise
     */
    boolean register(String username, String password);

    /**
     * Logs out the currently authenticated user
     */
    void logout();

    /**
     * Checks if a user is currently logged in
     * 
     * @return true if a user is logged in, false otherwise
     */
    boolean isLoggedIn();

    /**
     * Gets the currently authenticated user
     * 
     * @return The current User entity, null if no user is logged in
     */
    User getCurrentUser();

    /**
     * Validates the provided credentials and logs in the user if valid
     *
     * @param username The username of the user
     * @param password The password of the user
     * @return LoginResult object containing the result of the login attempt
     */
    LoginResult validateAndLogin(String username, String password);

    /**
     * Validates the provided credentials and registers the user if valid
     *
     * @param username The username for the new user
     * @param password The password for the new user
     * @return RegisterResult object containing the result of the registration attempt
     */
    RegisterResult validateAndRegister(String username, String password);


    }
