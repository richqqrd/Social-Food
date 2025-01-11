package com.example.socialfood.controller;

import android.content.Context;

import com.example.socialfood.model.entities.User;
import com.example.socialfood.utils.UserManager;

/**
 * Base controller class providing common functionality for all controllers. Manages user context
 * and access to the UserManager.
 */
public abstract class BaseController {
    /** Application context */
    protected final Context context;

    /** Manager for user-related operations */
    protected UserManager userManager;

    /**
     * Constructs a new BaseController
     * 
     * @param context Application context used to initialize UserManager
     */
    public BaseController(Context context) {
        this.context = context;
        this.userManager = UserManager.getInstance(context);
    }

    /**
     * Gets the currently authenticated user
     * 
     * @return The current User entity, null if no user is logged in
     */
    public User getCurrentUser() {
        return userManager.getCurrentUser();
    }

    /**
     * Gets the ID of the currently authenticated user
     * 
     * @return The current user's ID
     */
    public int getCurrentUserId() {
        return userManager.getCurrentUserId();
    }

    /**
     * Sets a custom UserManager instance (primarily used for testing)
     * 
     * @param userManager The UserManager instance to use
     */
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
