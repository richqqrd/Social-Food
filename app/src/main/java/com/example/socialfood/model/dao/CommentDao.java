package com.example.socialfood.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.socialfood.model.entities.Comment;

import java.util.List;

/**
 * Data Access Object (DAO) interface for Comment entity. Provides methods to perform CRUD
 * operations on the comment table.
 */
@Dao
public interface CommentDao {

    /**
     * Retrieves all comments from the database
     * 
     * @return List of all Comment entities
     */
    @Query("SELECT * FROM comment")
    List<Comment> getAll();

    /**
     * Inserts a new comment into the database
     * 
     * @param comment The Comment entity to insert
     */
    @Insert
    void insertComment(Comment comment);

    /**
     * Updates an existing comment in the database
     * 
     * @param comment The Comment entity to update
     */
    @Update
    void updateComment(Comment comment);

    /**
     * Deletes a comment from the database
     * 
     * @param comment The Comment entity to delete
     */
    @Delete
    void deleteComment(Comment comment);

    /**
     * Retrieves all comments for a specific post
     * 
     * @param postId The ID of the post
     * @return List of comments associated with the post
     */
    @Query("SELECT * FROM comment WHERE postId = :postId ORDER BY commentId")
    List<Comment> getCommentsByPostId(int postId);

    /**
     * Retrieves a specific comment by its composite key
     * 
     * @param uid The user ID of the comment creator
     * @param postId The post ID this comment belongs to
     * @param commentId The unique identifier of the comment
     * @return The Comment if found, null otherwise
     */
    @Query("SELECT * FROM comment WHERE uid = :uid AND postId = :postId AND commentId = :commentId")
    Comment getCommentById(int uid, int postId, int commentId);

}
