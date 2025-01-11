package com.example.socialfood.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.socialfood.model.entities.Post;

import java.util.List;

/**
 * Data Access Object (DAO) interface for Post entity. Provides methods to perform database
 * operations on posts.
 */
@Dao
public interface PostDao {

    /**
     * Inserts a new post into the database
     * 
     * @param post The Post entity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(Post post);

    /**
     * Updates an existing post in the database
     * 
     * @param post The Post entity to update
     */
    @Update
    void updatePost(Post post);

    /**
     * Deletes a post from the database
     * 
     * @param post The Post entity to delete
     */
    @Delete
    void deletePost(Post post);

    /**
     * Gets a specific post by user ID and post ID
     * 
     * @param uid The user ID
     * @param postId The post ID
     * @return The requested Post entity or null if not found
     */
    @Query("SELECT * FROM post WHERE uid = :uid AND postId = :postId")
    Post getPostById(int uid, int postId);

    /**
     * Gets all posts from a specific user
     * 
     * @param uid The user ID
     * @return List of posts by the user
     */
    @Query("SELECT * FROM post WHERE uid = :uid")
    List<Post> getPostByUser(int uid);

    /**
     * Gets all posts from the database ordered by timestamp (newest first)
     * 
     * @return List of all posts ordered by creation time descending
     */
    @Query("SELECT * FROM post ORDER BY timestamp DESC")
    List<Post> getAllPosts();
}
