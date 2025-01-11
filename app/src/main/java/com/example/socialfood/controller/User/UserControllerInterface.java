package com.example.socialfood.controller.User;

import com.example.socialfood.model.entities.User;

/**
 * Interface defining user-related operations for the social food application. Manages user
 * creation, updates, deletions and follow relationships.
 */
public interface UserControllerInterface {
    /**
     * Creates a new user in the system
     * 
     * @param user The User entity to create
     * @return The ID of the created user, -1 if creation failed
     */
    int createUser(User user);

    /**
     * Updates an existing user's information
     * 
     * @param user The User entity to update
     * @return true if update was successful, false otherwise
     */
    boolean updateUser(User user);

    /**
     * Deletes a user from the system
     * 
     * @param user The User entity to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteUser(User user);

    /**
     * Creates a follow relationship between current user and target user
     * 
     * @param userId The ID of the user to follow
     * @return true if follow operation was successful, false otherwise
     */
    boolean followUser(int userId);

    /**
     * Removes a follow relationship between current user and target user
     * 
     * @param userId The ID of the user to unfollow
     * @return true if unfollow operation was successful, false otherwise
     */
    boolean unfollowUser(int userId);

    /**
     * Sets the current active user in the system
     * 
     * @param user The User entity to set as current
     * @return true if operation was successful, false otherwise
     */
    boolean setCurrentUser(User user);

    /**
     * Gets the currently active user
     * 
     * @return The current User entity, null if no user is active
     */
    User getCurrentUser();

    /**
     * Gets the ID of the currently active user
     * 
     * @return The current user's ID
     */
    int getCurrentUserId();

    /**
     * Loads a user into the current session by ID
     * 
     * @param userId The ID of the user to load
     * @return true if user was loaded successfully, false otherwise
     */
    boolean loadCurrentUser(int userId);

    /**
     * Checks if the current user is following another user
     * 
     * @param userId The ID of the user to check
     * @return true if following, false otherwise
     */
    boolean isFollowing(int userId);

    /**
     * Retrieves a user by their ID
     * 
     * @param userId The ID of the user to retrieve
     * @return The User entity if found, null otherwise
     */
    User getUserById(int userId);
}
