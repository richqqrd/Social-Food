package com.example.socialfood.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.socialfood.model.entities.Follow;

import java.util.List;

/**
 * Data Access Object (DAO) interface for Follow entity. Provides methods to perform database
 * operations on follow relationships.
 */
@Dao
public interface FollowDao {

    /**
     * Inserts a new follow relationship into the database
     * 
     * @param follow The Follow entity to insert
     */
    @Insert
    void insert(Follow follow);

    /**
     * Deletes a follow relationship from the database
     * 
     * @param follow The Follow entity to delete
     */
    @Delete
    void delete(Follow follow);

    /**
     * Retrieves a specific follow relationship
     * 
     * @param followerId ID of the follower
     * @param followedId ID of the user being followed
     * @return The Follow entity if exists, null otherwise
     */
    @Query("SELECT * FROM follows WHERE followerId = :followerId AND followedId = :followedId")
    Follow getFollow(int followerId, int followedId);

    /**
     * Gets all users that a specific user is following
     * 
     * @param userId The ID of the user
     * @return List of Follow entities
     */
    @Query("SELECT * FROM follows WHERE followerId = :userId")
    List<Follow> getFollowingByUser(int userId);

    /**
     * Gets all followers of a specific user
     * 
     * @param userId The ID of the user
     * @return List of Follow entities
     */
    @Query("SELECT * FROM follows WHERE followedId = :userId")
    List<Follow> getFollowersByUser(int userId);

    /**
     * Checks if a user is following another user
     * 
     * @param followerId ID of the potential follower
     * @param followedId ID of the user potentially being followed
     * @return true if followerId is following followedId, false otherwise
     */
    @Query("SELECT EXISTS(SELECT 1 FROM follows WHERE followerId = :followerId AND followedId = :followedId)")
    boolean isFollowing(int followerId, int followedId);

    /**
     * Retrieves all follow relationships from the database
     * 
     * @return List of all Follow entities
     */
    @Query("SELECT * FROM follows")
    List<Follow> getAll();

}
