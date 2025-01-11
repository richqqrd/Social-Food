package com.example.socialfood.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.socialfood.model.handler.UserHandler;
import com.example.socialfood.model.entities.User;

/**
 * Singleton class responsible for managing user sessions and authentication.
 * Handles user login, logout, and persistent user data storage.
 */
public class UserManager {
    private static UserManager instance;
    private User currentUser;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_USER_ID = "userId";
    private UserHandler userHandler;

    /**
     * Sets the singleton instance. Used primarily for testing.
     *
     * @param mockManager The mock UserManager instance to set
     */
    public static void setInstance(UserManager mockManager) {
        instance = mockManager;
    }

    /**
     * Private constructor for singleton pattern.
     * Initializes SharedPreferences and UserHandler.
     *
     * @param context Application context used for initialization
     */
    private UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userHandler = new UserHandler(context);
        loadSavedUser();
    }

    /**
     * Gets the singleton instance of UserManager.
     * Creates a new instance if none exists.
     *
     * @param context Application context needed for initialization
     * @return The singleton instance of UserManager
     */
    public static UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context);
        }
        return instance;
    }

    /**
     * Sets the user handler for dependency injection in tests
     * 
     * @param userHandler The handler to be used for user operations
     */
    public void setUserHandler(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    /**
     * Loads the previously saved user from SharedPreferences.
     * If a user ID is found, loads the corresponding user from the database.
     */
    void loadSavedUser() {
        int savedUserId = sharedPreferences.getInt(KEY_USER_ID, -1);
        if (savedUserId != -1) {
            currentUser = userHandler.getUserById(savedUserId);
        }
    }

    /**
     * Logs in a user and saves their ID to SharedPreferences.
     *
     * @param user The user to log in
     */
    public void loginUser(User user) {
        this.currentUser = user;
        sharedPreferences.edit().putInt(KEY_USER_ID, user.getUid()).apply();
    }

    /**
     * Logs out the current user.
     * Clears the current user and removes the saved user ID from SharedPreferences.
     */
    public void logoutUser() {
        this.currentUser = null;
        sharedPreferences.edit().remove(KEY_USER_ID).apply();
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Gets the currently logged in user.
     *
     * @return The current User entity, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Gets the ID of the currently logged in user.
     *
     * @return The current user's ID, or -1 if no user is logged in
     */
    public int getCurrentUserId() {
        return currentUser != null ? currentUser.getUid() : -1;
    }
}
