package com.example.socialfood.model.handler;

import android.content.Context;
import android.util.Log;

import com.example.socialfood.model.dao.UserDao;
import com.example.socialfood.model.database.DatabaseClient;
import com.example.socialfood.model.entities.User;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Handler class for User entities. Manages database operations for users through UserDao.
 * Implements EntityHandlerInterface for standard CRUD operations.
 */
public class UserHandler implements EntityHandlerInterface<User> {
    private final UserDao userDao;
    private final ExecutorService executorService;
    private static final String TAG = "UserHandler";
    private static final int TIMEOUT_SECONDS = 5;

    /**
     * Constructs a new UserHandler
     * 
     * @param context The application context
     */
    public UserHandler(Context context) {
        this.userDao = DatabaseClient.getInstance(context).getDatabase().userDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public UserHandler(DatabaseClient databaseClient) {
        this.userDao = databaseClient.getDatabase().userDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Inserts a new user into the database
     * 
     * @param entity The User entity to insert
     * @return true if insertion was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean insert(User entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot insert null user");
            return false;
        }
        Log.d(TAG, "Inserting user: " + entity.getUsername());
        try {
            Future<?> future = executorService.submit(() -> userDao.insertUser(entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Successfully inserted user: " + entity.getUsername());
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting user", e);
            return false;
        }
    }

    /**
     * Retrieves all users from the database
     * 
     * @return List of all users, empty list if none found or on error
     */
    @Override
    public List<User> getAll() {
        try {
            Future<List<User>> future = executorService.submit(() -> userDao.getAll());
            List<User> users = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Retrieved " + (users != null ? users.size() : 0) + " users");
            return users != null ? users : Collections.emptyList();
        } catch (Exception e) {
            Log.e(TAG, "Error getting all users", e);
            return Collections.emptyList();
        }
    }

    /**
     * Updates an existing user in the database
     * 
     * @param entity The User entity to update
     * @return true if update was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean update(User entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot update null user");
            return false;
        }
        try {
            Future<?> future = executorService.submit(() -> userDao.updateUsers(entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Successfully updated user: " + entity.getUsername());
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error updating user", e);
            return false;
        }
    }

    /**
     * Deletes a user from the database
     * 
     * @param entity The User entity to delete
     * @return true if deletion was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean delete(User entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot delete null user");
            return false;
        }
        try {
            Future<?> future = executorService.submit(() -> userDao.deleteUser(entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Successfully deleted user: " + entity.getUsername());
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting user", e);
            return false;
        }
    }

    /**
     * Retrieves a user by their username
     * 
     * @param username The username to search for
     * @return The User if found, null otherwise
     * @throws IllegalArgumentException if username is null or empty
     */
    public User getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            Log.e(TAG, "Invalid username");
            return null;
        }
        try {
            Future<User> future = executorService.submit(() -> userDao.getUserByUsername(username));
            User user = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Retrieved user: " + (user != null ? user.getUsername() : "not found"));
            return user;
        } catch (Exception e) {
            Log.e(TAG, "Error getting user by username: " + username, e);
            return null;
        }
    }

    /**
     * Retrieves a user by their ID
     * 
     * @param uid The user ID to search for
     * @return The User if found, null otherwise
     * @throws IllegalArgumentException if uid is not positive
     */
    public User getUserById(int uid) {
        if (uid <= 0) {
            Log.e(TAG, "Invalid user ID");
            return null;
        }
        try {
            Future<User> future = executorService.submit(() -> userDao.getUserbyId(uid));
            User user = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Retrieved user: " + (user != null ? user.getUsername() : "not found"));
            return user;
        } catch (Exception e) {
            Log.e(TAG, "Error getting user by id: " + uid, e);
            return null;
        }
    }
}
