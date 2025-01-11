package com.example.socialfood.model.handler;

import android.content.Context;
import android.util.Log;

import com.example.socialfood.model.dao.PostDao;
import com.example.socialfood.model.database.DatabaseClient;
import com.example.socialfood.model.entities.Post;
import com.example.socialfood.model.entities.User;
import com.example.socialfood.model.dao.LikeDao;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Handler class for Post entities. Manages database operations for posts through PostDao.
 * Implements EntityHandlerInterface for standard CRUD operations.
 */
public class PostHandler implements EntityHandlerInterface<Post> {
    private final PostDao postDao;
    private final LikeDao likeDao;
    private final ExecutorService executorService;
    private static final String TAG = "PostHandler";
    private static final int TIMEOUT_SECONDS = 5;

    /**
     * Constructs a new PostHandler
     * 
     * @param context The application context
     */
    public PostHandler(Context context) {
        this.postDao = DatabaseClient.getInstance(context).getDatabase().postDao();
        this.executorService = Executors.newSingleThreadExecutor();
        this.likeDao = DatabaseClient.getInstance(context).getDatabase().likeDao();
    }

    public PostHandler(DatabaseClient databaseClient) {
        this.postDao = databaseClient.getDatabase().postDao();
        this.executorService = Executors.newSingleThreadExecutor();
        this.likeDao = databaseClient.getDatabase().likeDao();
    }

    /**
     * Inserts a new post into the database
     * 
     * @param entity The Post entity to insert
     * @return true if insertion was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean insert(Post entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot insert null post");
            return false;
        }
        try {
            Future<?> future = executorService.submit(() -> postDao.insertPost((Post) entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Successfully inserted post: " + entity.getPostId());

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting post", e);
            return false;
        }
    }

    /**
     * Retrieves all posts from the database
     * 
     * @return List of all posts, empty list if none found or on error
     */
    @Override
    public List<Post> getAll() {
        try {
            Future<List<Post>> future = executorService.submit(() -> postDao.getAllPosts());

            List<Post> posts = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Retrieved " + (posts != null ? posts.size() : 0) + " posts");
            return posts != null ? posts : Collections.emptyList();
        } catch (Exception e) {
            Log.e(TAG, "Error getting all posts", e);
            return Collections.emptyList();
        }
    }

    /**
     * Updates an existing post in the database
     * 
     * @param entity The Post entity to update
     * @return true if update was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean update(Post entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot update null post");
            return false;
        }
        try {
            Future<?> future = executorService.submit(() -> postDao.updatePost((Post) entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Successfully updated post: " + entity.getPostId());

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error updating post", e);
            return false;
        }
    }

    /**
     * Deletes a post from the database
     * 
     * @param entity The Post entity to delete
     * @return true if deletion was successful, false otherwise
     * @throws IllegalArgumentException if entity is null
     */
    @Override
    public boolean delete(Post entity) {
        if (entity == null) {
            Log.e(TAG, "Cannot delete null post");
            return false;
        }
        try {
            Future<?> future = executorService.submit(() -> postDao.deletePost(entity));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Successfully deleted post: " + entity.getPostId());
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting post", e);
            return false;
        }
    }

    /**
     * Retrieves a specific post by its composite key (uid and postId)
     * 
     * @param uid The user ID who created the post
     * @param postId The unique identifier of the post
     * @return The Post entity if found, null otherwise
     */
    public Post getPostById(int uid, int postId) {
        if (uid <= 0 || postId <= 0) {
            Log.e(TAG, "Invalid user ID or post ID");
            return null;
        }
        try {
            Future<Post> future = executorService.submit(() -> postDao.getPostById(uid, postId));
            Post post = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Retrieved post: " + (post != null ? post.getPostId() : "not found"));
            return post;
        } catch (Exception e) {
            Log.e(TAG, "Error getting post by id", e);
            return null;
        }
    }

    /**
     * Retrieves all posts for a specific user
     * 
     * @param uid The user ID to get posts for
     * @return List of posts by the user, empty list if none found or on error
     */
    public List<Post> getPostByUser(int uid) {
        if (uid <= 0) {
            Log.e(TAG, "Invalid user ID");
            return Collections.emptyList();
        }
        try {
            Future<List<Post>> future = executorService.submit(() -> postDao.getPostByUser(uid));
            List<Post> posts = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG,
                    "Retrieved " + (posts != null ? posts.size() : 0) + " posts for user " + uid);
            return posts != null ? posts : Collections.emptyList();
        } catch (Exception e) {
            Log.e(TAG, "Error getting posts by user", e);
            return Collections.emptyList();
        }
    }

    /**
     * Checks if a post is liked by a specific user
     * 
     * @param post The Post entity to check
     * @param user The User entity to check
     * @return true if the post is liked by the user, false otherwise or on error
     */
    public boolean isLikedByUser(Post post, User user) {
        if (post == null || user == null) {
            Log.e(TAG, "Cannot check like status for null post or user");
            return false;
        }
        try {
            Future<Boolean> future = executorService
                    .submit(() -> likeDao.isLikedByUser(user.getUid(), post.getPostId()));
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error checking if post is liked by user", e);
            return false;
        }
    }

    /**
     * Retrieves all posts from the database with additional information This method might include
     * more data than the basic getAll() method
     * 
     * @return List of all posts with additional information, empty list if none found or on error
     */
    public List<Post> getAllPosts() {
        try {
            Future<List<Post>> future = executorService.submit(() -> postDao.getAllPosts());
            List<Post> posts = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Log.d(TAG, "Retrieved " + (posts != null ? posts.size() : 0) + " posts");
            return posts != null ? posts : Collections.emptyList();
        } catch (Exception e) {
            Log.e(TAG, "Error getting all posts", e);
            return Collections.emptyList();
        }
    }

}
