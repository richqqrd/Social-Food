package com.example.socialfood.model.handler;

import android.content.Context;
import android.util.Log;

import com.example.socialfood.model.dao.CommentDao;
import com.example.socialfood.model.database.DatabaseClient;
import com.example.socialfood.model.entities.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Handler class for Comment entities. Manages database operations for comments through CommentDao.
 * Implements EntityHandlerInterface for standard CRUD operations.
 */
public class CommentHandler implements EntityHandlerInterface<Comment> {
    private final CommentDao commentDao;
    private final ExecutorService executorService;
    private static final String TAG = "CommentHandler";
    private static final int TIMEOUT_SECONDS = 5;

    /**
     * Constructs a new CommentHandler
     *
     * @param context The application context
     */
    public CommentHandler(Context context) {
        this.commentDao = DatabaseClient.getInstance(context).getDatabase().commentDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public CommentHandler(DatabaseClient databaseClient) {
        this.commentDao = databaseClient.getDatabase().commentDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Inserts a new comment into the database
     *
     * @param entity The comment to insert
     * @return true if insertion was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean insert(Comment entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot insert null comment");
            return false;
        }
        try {
            Future<?> future = executorService.submit(() -> commentDao.insertComment(entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting comment", e);
            return false;
        }
    }

    /**
     * Retrieves all comments from the database
     *
     * @return List of all comments, empty list if none found or on error
     */
    @Override
    public List<Comment> getAll() {
        try {
            Future<List<Comment>> future = executorService.submit(() -> commentDao.getAll());
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error getting all comments", e);
            return Collections.emptyList();
        }
    }

    /**
     * Updates an existing comment in the database
     *
     * @param entity The comment to update
     * @return true if update was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean update(Comment entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot update null comment");
            return false;
        }
        try {
            Future<?> future = executorService.submit(() -> commentDao.updateComment(entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error updating comment", e);
            return false;
        }
    }

    /**
     * Deletes a comment from the database
     *
     * @param entity The comment to delete
     * @return true if deletion was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean delete(Comment entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot delete null comment");
            return false;
        }
        try {
            Future<?> future = executorService.submit(() -> commentDao.deleteComment(entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting comment", e);
            return false;
        }
    }

    /**
     * Retrieves a specific comment by its composite key
     *
     * @param uid The user ID of the comment creator
     * @param postId The post ID this comment belongs to
     * @param commentId The unique identifier of the comment
     * @return The Comment if found, null otherwise
     * @throws IllegalArgumentException if any ID is not positive
     */
    public Comment getCommentById(int uid, int postId, int commentId) {
        if (uid <= 0 || postId <= 0 || commentId <= 0) {
            Log.e(TAG, "Invalid ID parameters");
            return null;
        }
        try {
            Future<Comment> future = executorService
                    .submit(() -> commentDao.getCommentById(uid, postId, commentId));
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error getting comment by id", e);
            return null;
        }
    }

    /**
     * Retrieves all comments for a specific post
     *
     * @param postId The ID of the post
     * @return List of comments, empty list if none found or on error
     * @throws IllegalArgumentException if postId is not positive
     */
    public List<Comment> getCommentsByPostId(int postId) {
        if (postId <= 0) {
            Log.e(TAG, "Invalid post ID");
            return new ArrayList<>();
        }
        try {
            Future<List<Comment>> future = executorService
                    .submit(() -> {
                        List<Comment> comments = commentDao.getCommentsByPostId(postId);
                        return comments;
                    });
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error getting comments for post", e);
            return new ArrayList<>();
        }
    }

}
