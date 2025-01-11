package com.example.socialfood.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.socialfood.model.dao.CommentDao;
import com.example.socialfood.model.dao.FollowDao;
import com.example.socialfood.model.dao.LikeDao;
import com.example.socialfood.model.dao.PostDao;
import com.example.socialfood.model.dao.UserDao;
import com.example.socialfood.model.entities.Comment;
import com.example.socialfood.model.entities.Follow;
import com.example.socialfood.model.entities.Like;
import com.example.socialfood.model.entities.Post;
import com.example.socialfood.model.entities.User;

/**
 * Main database class for the application. Handles all database operations through DAOs for
 * different entities.
 */
@Database(entities = { User.class, Post.class, Comment.class, Like.class,
        Follow.class }, version = 9)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Gets the DAO for User operations
     * 
     * @return UserDao instance for handling user-related database operations
     */
    public abstract UserDao userDao();

    /**
     * Gets the DAO for Post operations
     * 
     * @return PostDao instance for handling post-related database operations
     */
    public abstract PostDao postDao();

    /**
     * Gets the DAO for Comment operations
     * 
     * @return CommentDao instance for handling comment-related database operations
     */
    public abstract CommentDao commentDao();

    /**
     * Gets the DAO for Like operations
     * 
     * @return LikeDao instance for handling like-related database operations
     */
    public abstract LikeDao likeDao();

    /**
     * Gets the DAO for Follow operations
     * 
     * @return FollowDao instance for handling follow relationship database operations
     */
    public abstract FollowDao followDao();
}
