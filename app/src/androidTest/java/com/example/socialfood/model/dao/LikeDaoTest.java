package com.example.socialfood.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.socialfood.model.database.AppDatabase;
import com.example.socialfood.model.entities.Like;
import com.example.socialfood.model.entities.Post;
import com.example.socialfood.model.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Test class for the LikeDao interface. Tests all CRUD operations and special queries. Uses Room's
 * in-memory database for testing.
 */
public class LikeDaoTest {
    private AppDatabase database;
    private LikeDao likeDao;
    private UserDao userDao;
    private PostDao postDao;

    /**
     * Sets up the test environment before each test. Creates an in-memory database and initializes
     * the LikeDao. Also creates necessary test data (user and post) to satisfy foreign key
     * constraints.
     */
    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        likeDao = database.likeDao();
        userDao = database.userDao();
        postDao = database.postDao();

        // Setup required user and post
        User user = createTestUser(1, "testuser");
        userDao.insertUser(user);

        Post post = createTestPost(1, 1);
        postDao.insertPost(post);
    }

    /**
     * Cleans up the test environment after each test. Closes the in-memory database.
     */
    @After
    public void teardown() {
        database.close();
    }

    /**
     * Tests inserting a like and basic retrieval
     */
    @Test
    public void testInsertLike() {
        Like like = createTestLike(1, 1);
        likeDao.insert(like);

        boolean exists = likeDao.isLikedByUser(1, 1);
        assertTrue("Post should be liked by user", exists);
    }

    /**
     * Tests retrieving all likes
     */
    @Test
    public void testGetAllLikes() {
        Like like1 = createTestLike(1, 1);
        likeDao.insert(like1);

        List<Like> likes = likeDao.getAll();
        assertEquals("Should have 1 like", 1, likes.size());
    }

    /**
     * Tests getting likes for a specific post
     */
    @Test
    public void testGetLikesForPost() {
        Like like = createTestLike(1, 1);
        likeDao.insert(like);

        List<Like> likes = likeDao.getLikesForPost(1);
        assertEquals("Post should have 1 like", 1, likes.size());
    }

    /**
     * Tests getting like count for a post
     */
    @Test
    public void testGetLikeCount() {
        Like like = createTestLike(1, 1);
        likeDao.insert(like);

        int count = likeDao.getLikeCount(1);
        assertEquals("Post should have 1 like", 1, count);
    }

    /**
     * Tests checking if a post is liked by a user
     */
    @Test
    public void testIsLikedByUser() {
        Like like = createTestLike(1, 1);
        likeDao.insert(like);

        boolean isLiked = likeDao.isLikedByUser(1, 1);
        assertTrue("Post should be liked by user", isLiked);
    }

    /**
     * Tests deleting a like
     */
    @Test
    public void testDeleteLike() {
        Like like = createTestLike(1, 1);
        likeDao.insert(like);

        likeDao.delete(like);

        boolean exists = likeDao.isLikedByUser(1, 1);
        assertFalse("Like should be deleted", exists);
    }

    /**
     * Tests exception handling for null like insertion
     */
    @Test(expected = NullPointerException.class)
    public void testInsertNullLike() {
        Like like = null;
        likeDao.insert(like);
    }

    /**
     * Tests exception handling for duplicate like
     */
    @Test(expected = SQLiteConstraintException.class)
    public void testInsertDuplicateLike() {
        Like like1 = createTestLike(1, 1);
        Like like2 = createTestLike(1, 1); // Same user and post IDs

        likeDao.insert(like1);
        likeDao.insert(like2); // Should throw exception
    }

    /**
     * Tests exception handling for invalid foreign key
     */
    @Test(expected = SQLiteConstraintException.class)
    public void testInsertLikeWithInvalidForeignKey() {
        Like like = createTestLike(999, 999); // Non-existent user and post IDs
        likeDao.insert(like);
    }

    /**
     * Helper method to create a test user
     */
    private User createTestUser(int uid, String username) {
        User user = new User();
        user.setUid(uid);
        user.setUsername(username);
        user.setPassword("password");
        user.setProfilImage("default.png");
        return user;
    }

    /**
     * Helper method to create a test post
     */
    private Post createTestPost(int uid, int postId) {
        Post post = new Post();
        post.setUid(uid);
        post.setPostId(postId);
        post.setImageUrl("default.png");
        post.setDescription("Test post");
        post.setTimestamp(System.currentTimeMillis());
        post.setCommentCount(0);
        post.setLatitude(0.0);
        post.setLongitude(0.0);
        return post;
    }

    /**
     * Helper method to create a test like
     */
    private Like createTestLike(int userId, int postId) {
        Like like = new Like();
        like.setUserId(userId);
        like.setPostId(postId);
        like.setTimestamp(System.currentTimeMillis());
        return like;
    }
}