package com.example.socialfood.model.handler;

import android.content.Context;
import android.util.Log;

import com.example.socialfood.model.dao.LikeDao;
import com.example.socialfood.model.database.DatabaseClient;
import com.example.socialfood.model.entities.Like;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Handler class for Like entities. Manages database operations for likes through LikeDao.
 * Implements EntityHandlerInterface for standard CRUD operations.
 */
public class LikeHandler implements EntityHandlerInterface<Like> {
    private final LikeDao likeDao;
    private final ExecutorService executorService;
    private static final String TAG = "LikeHandler";
    private static final int TIMEOUT_SECONDS = 5;

    /**
     * Constructs a new LikeHandler
     * 
     * @param context The application context
     */
    public LikeHandler(Context context) {
        this.likeDao = DatabaseClient.getInstance(context).getDatabase().likeDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LikeHandler(DatabaseClient databaseClient) {
        this.likeDao = databaseClient.getDatabase().likeDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Inserts a new like into the database
     * 
     * @param entity The Like entity to insert
     * @return true if insertion was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean insert(Like entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot insert null like");
            return false;
        }
        try {
            Future<?> future = executorService.submit(() -> likeDao.insert(entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting like", e);
            return false;
        }
    }

    /**
     * Retrieves all likes from the database
     * 
     * @return List of all likes, empty list if none found or on error
     */
    @Override
    public List<Like> getAll() {
        try {
            Future<List<Like>> future = executorService.submit(() -> likeDao.getAll());
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error getting all likes", e);
            return Collections.emptyList();
        }
    }

    /**
     * Updates an existing like in the database
     * 
     * @param entity The Like entity to update
     * @return true if update was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean update(Like entity) {
        Log.w(TAG, "Update operation not supported for likes - use insert/delete instead");
        return false;
    }

    /**
     * Deletes a like from the database
     * 
     * @param entity The Like entity to delete
     * @return true if deletion was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean delete(Like entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot delete null like");
            return false;
        }
        try {
            Future<?> future = executorService.submit(() -> likeDao.delete(entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting like", e);
            return false;
        }
    }

    /**
     * Toggles a like for a post by a user. If the post is already liked by the user, the like is
     * removed. Otherwise, a new like is created.
     *
     * @param userId The ID of the user toggling the like
     * @param postId The ID of the post being liked/unliked
     * @return true if the post was liked, false if it was unliked or on error
     */
    public boolean toggleLike(int userId, int postId) {
        try {
            Future<Boolean> future = executorService.submit(() -> {
                if (likeDao.isLikedByUser(userId, postId)) {
                    Like like = new Like();
                    like.setUserId(userId);
                    like.setPostId(postId);
                    likeDao.delete(like);
                    Log.d(TAG, "Like removed for post " + postId + " by user " + userId);
                    return false;
                } else {
                    Like like = new Like();
                    like.setUserId(userId);
                    like.setPostId(postId);
                    like.setTimestamp(System.currentTimeMillis());
                    likeDao.insert(like);
                    Log.d(TAG, "Like added for post " + postId + " by user " + userId);
                    return true;
                }
            });
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error toggling like for post " + postId, e);
            return false;
        }
    }

    /**
     * Checks if a post is liked by a specific user
     * 
     * @param userId The ID of the user
     * @param postId The ID of the post
     * @return true if the post is liked by the user, false otherwise or on error
     */
    public boolean isLikedByUser(int userId, int postId) {
        if (userId <= 0 || postId <= 0) {
            Log.e(TAG, "Invalid user ID or post ID");
            return false;
        }
        try {
            Future<Boolean> future = executorService
                    .submit(() -> likeDao.isLikedByUser(userId, postId));
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error checking like status for post " + postId, e);
            return false;
        }
    }

    /**
     * Gets the total number of likes for a post
     * 
     * @param postId The ID of the post
     * @return The number of likes for the post, 0 if post doesn't exist or on error
     */
    public int getLikeCount(int postId) {
        if (postId <= 0) {
            Log.e(TAG, "Invalid post ID");
            return 0;
        }
        try {
            Future<Integer> future = executorService.submit(() -> likeDao.getLikeCount(postId));
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error getting like count for post " + postId, e);
            return 0;
        }
    }

}