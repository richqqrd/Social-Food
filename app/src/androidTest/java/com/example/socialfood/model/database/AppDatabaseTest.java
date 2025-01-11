package com.example.socialfood.model.database;

import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.socialfood.model.dao.CommentDao;
import com.example.socialfood.model.dao.FollowDao;
import com.example.socialfood.model.dao.LikeDao;
import com.example.socialfood.model.dao.PostDao;
import com.example.socialfood.model.dao.UserDao;
import com.example.socialfood.model.database.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Integration tests for the AppDatabase class.
 */
@RunWith(JUnit4.class)
public class AppDatabaseTest {
    private AppDatabase database;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void teardown() {
        database.close();
    }

    /**
     * Tests retrieving UserDao instance
     */
    @Test
    public void testGetUserDao() {
        UserDao userDao = database.userDao();
        assertNotNull("UserDao should not be null", userDao);
    }

    /**
     * Tests retrieving PostDao instance
     */
    @Test
    public void testGetPostDao() {
        PostDao postDao = database.postDao();
        assertNotNull("PostDao should not be null", postDao);
    }

    /**
     * Tests retrieving CommentDao instance
     */
    @Test
    public void testGetCommentDao() {
        CommentDao commentDao = database.commentDao();
        assertNotNull("CommentDao should not be null", commentDao);
    }

    /**
     * Tests retrieving LikeDao instance
     */
    @Test
    public void testGetLikeDao() {
        LikeDao likeDao = database.likeDao();
        assertNotNull("LikeDao should not be null", likeDao);
    }

    /**
     * Tests retrieving FollowDao instance
     */
    @Test
    public void testGetFollowDao() {
        FollowDao followDao = database.followDao();
        assertNotNull("FollowDao should not be null", followDao);
    }
}