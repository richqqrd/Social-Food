package com.example.socialfood.controller.Post;

import com.example.socialfood.model.entities.Comment;
import com.example.socialfood.model.entities.Post;

import java.util.List;

/**
 * Interface defining post-related operations for the social food application. Handles post
 * creation, retrieval, updates, and interactions (likes/comments).
 */
public interface PostControllerInterface {
    /**
     * Creates a new post with the provided details
     * 
     * @param photoPath Path to the post's photo
     * @param description Text description of the post
     * @param recipe Recipe instructions
     * @param ingredients List of ingredients
     * @param latitude Geographic latitude of the post
     * @param longitude Geographic longitude of the post
     * @return true if post creation was successful, false otherwise
     */
    boolean createPost(String photoPath, String description, String recipe, String ingredients,
            double latitude, double longitude);

    /**
     * Retrieves a post by its ID
     * 
     * @param postId The ID of the post to retrieve
     * @return The Post if found, null otherwise
     */
    Post getPost(int postId);

    /**
     * Gets all posts from a specific user
     * 
     * @param uid The user ID whose posts to retrieve
     * @return List of posts by the user
     */
    List<Post> getPostsFromUser(int uid);

    /**
     * Gets the number of likes for a post
     * 
     * @param postId The ID of the post
     * @return Number of likes
     */
    int getLikeCount(int postId);

    /**
     * Updates an existing post
     * 
     * @param post The Post entity to update
     * @return true if update was successful, false otherwise
     */
    boolean updatePost(Post post);

    /**
     * Deletes a post
     * 
     * @param post The Post entity to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deletePost(Post post);

    /**
     * Checks if the current user has liked a post
     * 
     * @param postId The ID of the post to check
     * @return true if user has liked the post, false otherwise
     */
    boolean isPostLikedByUser(int postId);

    /**
     * Retrieves a post by user ID and post ID
     * 
     * @param uid The user ID of the post creator
     * @param postId The ID of the post
     * @return The Post if found, null otherwise
     */
    Post getPostById(int uid, int postId);

    /**
     * Handles liking/unliking a post
     * 
     * @param postId The ID of the post to like/unlike
     */
    void onLikePost(int postId);

    /**
     * Adds a comment to a post
     * 
     * @param postId The ID of the post to comment on
     * @param commentText The text content of the comment
     */
    void onCommentPost(int postId, String commentText);

    /**
     * Gets all comments for a post
     * 
     * @param postId The ID of the post
     * @return List of comments on the post
     */
    List<Comment> getCommentsForPost(int postId);

    /**
     * Retrieves all posts in the system
     * 
     * @return List of all posts
     */
    List<Post> getAllPosts();
}
