package com.example.socialfood.model.handler;

import android.content.Context;
import android.util.Log;

import com.example.socialfood.model.dao.FollowDao;
import com.example.socialfood.model.database.DatabaseClient;
import com.example.socialfood.model.entities.Follow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Handler class for Follow entities. Manages database operations for follow relationships.
 * Implements EntityHandlerInterface for standard CRUD operations.
 */
public class FollowHandler implements EntityHandlerInterface<Follow> {
    private final FollowDao followDAO;
    private final ExecutorService executorService;
    private static final String TAG = "FollowHandler";
    private static final int TIMEOUT_SECONDS = 5;

    /**
     * Constructs a new FollowHandler
     * 
     * @param context The application context
     */
    public FollowHandler(Context context) {
        this.followDAO = DatabaseClient.getInstance(context).getDatabase().followDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public FollowHandler(DatabaseClient databaseClient) {
        this.followDAO = databaseClient.getDatabase().followDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Inserts a new follow relationship
     * 
     * @param entity The Follow entity to insert
     * @return true if insertion was successful, false otherwise
     */
    public boolean insert(Follow entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot insert null follow relationship");
            return false;
        }
        try {
            Future<Void> future = executorService.submit(() -> {
                followDAO.insert(entity);
                return null;
            });
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting follow relationship", e);
            return false;
        }
    }

    /**
     * Retrieves all follow relationships from the database. Uses an executor service to perform the
     * database operation asynchronously.
     * 
     * @return List of all Follow entities, empty list if none found or on error
     */
    @Override
    public List<Follow> getAll() {
        try {
            Future<List<Follow>> future = executorService.submit(() -> followDAO.getAll());
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error getting all follow relationships", e);
            return new ArrayList<>();
        }
    }

    /**
     * Updates a follow relationship
     * 
     * @param entity The Follow entity to update
     * @return true if update was successful
     */
    @Override
    public boolean update(Follow entity) {
        Log.w(TAG,
                "Update operation not supported for follow relationships - use insert/delete instead");
        return false;
    }

    /**
     * Deletes a follow relationship from the database
     * 
     * @param entity The Follow entity to delete
     * @return true if deletion was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean delete(Follow entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot delete null follow relationship");
            return false;
        }
        try {
            Future<?> future = executorService.submit(() -> followDAO.delete(entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting follow relationship", e);
            return false;
        }
    }

    /**
     * Deletes a follow relationship from the database
     * 
     * @param followerId The ID of the follower user
     * @param followedId The ID of the user being followed
     * @return true if deletion was successful, false otherwise
     * @throws IllegalArgumentException if either ID is not positive
     */
    public boolean delete(int followerId, int followedId) {
        if (followerId <= 0 || followedId <= 0) {
            Log.e(TAG, "Invalid follower or followed ID");
            return false;
        }
        try {
            Future<Boolean> future = executorService.submit(() -> {
                Follow follow = followDAO.getFollow(followerId, followedId);
                if (follow != null) {
                    followDAO.delete(follow);
                    return true;
                }
                return false;
            });
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error deleting follow relationship", e);
            return false;
        }
    }

    /**
     * Checks if a follow relationship exists
     * 
     * @param followerId ID of potential follower
     * @param followedId ID of potentially followed user
     * @return true if relationship exists, false otherwise
     */
    public boolean exists(int followerId, int followedId) {
        if (followerId <= 0 || followedId <= 0) {
            Log.e(TAG, "Invalid follower or followed ID");
            return false;
        }
        try {
            Future<Boolean> future = executorService
                    .submit(() -> followDAO.isFollowing(followerId, followedId));
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error checking follow status", e);
            return false;
        }
    }

    /**
     * Gets all users that a specific user is following
     * 
     * @param userId The ID of the user
     * @return List of Follow entities
     */
    public List<Follow> getFollowingByUser(int userId) {
        if (userId <= 0) {
            Log.e(TAG, "Invalid user ID");
            return new ArrayList<>();
        }
        try {
            Future<List<Follow>> future = executorService
                    .submit(() -> followDAO.getFollowingByUser(userId));
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error getting following list", e);
            return new ArrayList<>();
        }
    }

    /**
     * Gets all followers of a specific user
     * 
     * @param userId The ID of the user
     * @return List of Follow entities
     */
    public List<Follow> getFollowersByUser(int userId) {
        if (userId <= 0) {
            Log.e(TAG, "Invalid user ID");
            return new ArrayList<>();
        }
        try {
            Future<List<Follow>> future = executorService
                    .submit(() -> followDAO.getFollowersByUser(userId));
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error getting followers list", e);
            return new ArrayList<>();
        }
    }
}