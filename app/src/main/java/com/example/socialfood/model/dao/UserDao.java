package com.example.socialfood.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.socialfood.model.entities.User;

import java.util.List;

/**
 * Data Access Object (DAO) interface for User entity. Provides methods to perform database
 * operations on users.
 */
@Dao
public interface UserDao {

    /**
     * Retrieves all users from the database
     * 
     * @return List of all User entities
     */
    @Query("SELECT * FROM user")
    List<User> getAll();

    /**
     * Inserts a new user into the database
     * 
     * @param user The User entity to insert
     */
    @Insert
    void insertUser(User users);

    /**
     * Updates an existing user in the database
     * 
     * @param user The User entity to update
     */
    @Update
    void updateUsers(User user);

    /**
     * Deletes a user from the database
     * 
     * @param user The User entity to delete
     */
    @Delete
    void deleteUser(User user);

    /**
     * Retrieves a user by their username
     * 
     * @param username The username to search for
     * @return The User entity if found, null otherwise
     */
    @Query("SELECT * FROM user WHERE username = :username")
    User getUserByUsername(String username);

    /**
     * Retrieves a user by their ID
     * 
     * @param uid The user ID to search for
     * @return The User entity if found, null otherwise
     */
    @Query("SELECT * FROM user where uid = :uid")
    User getUserbyId(int uid);

}
