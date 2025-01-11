package com.example.socialfood.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.socialfood.model.entities.Like;

import java.util.List;

/**
 * Data Access Object (DAO) interface for Like entity. Provides methods to perform database
 * operations on likes.
 */
@Dao
public interface LikeDao {
    /**
     * Inserts a new like into the database
     * 
     * @param like The Like entity to insert
     */
    @Insert
    void insert(Like like);

    /**
     * Deletes a like from the database
     * 
     * @param like The Like entity to delete
     */
    @Delete
    void delete(Like like);

    /**
     * Gets all likes for a specific post
     * 
     * @param postId The ID of the post
     * @return List of likes associated with the post
     */
    @Query("SELECT * FROM `like_table` WHERE postId = :postId")
    List<Like> getLikesForPost(int postId);

    /**
     * Gets the total number of likes for a post
     * 
     * @param postId The ID of the post
     * @return The number of likes
     */
    @Query("SELECT COUNT(*) FROM `like_table` WHERE postId = :postId")
    int getLikeCount(int postId);

    /**
     * Checks if a user has liked a specific post
     * 
     * @param userId The ID of the user
     * @param postId The ID of the post
     * @return true if the user has liked the post, false otherwise
     */
    @Query("SELECT EXISTS(SELECT 1 FROM `like_table` WHERE userId = :userId AND postId = :postId)")
    boolean isLikedByUser(int userId, int postId);

    /**
     * Retrieves all likes from the database
     * 
     * @return List of all Like entities
     */
    @Query("SELECT * FROM like_table")
    List<Like> getAll();
}