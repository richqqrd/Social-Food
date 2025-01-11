package com.example.socialfood.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.socialfood.model.database.AppDatabase;
import com.example.socialfood.model.entities.Post;
import com.example.socialfood.model.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Test class for the PostDao interface. Tests all CRUD (Create, Read, Update, Delete) operations
 * and special queries. Uses Room's in-memory database for testing.
 */
public class PostDaoTest {
    private AppDatabase database;
    private PostDao postDao;
    private UserDao userDao;

    /**
     * Sets up the test environment before each test. Creates an in-memory database and initializes
     * the PostDao. Also creates necessary test data (user) to satisfy foreign key constraints.
     */
    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        postDao = database.postDao();
        userDao = database.userDao();

        // Setup required user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setProfilImage("default.png");
        userDao.insertUser(user);
    }

    /**
     * Cleans up the test environment after each test. Closes the in-memory database.
     */
    @After
    public void teardown() {
        database.close();
    }

    /**
     * Tests inserting and retrieving a post
     */
    @Test
    public void testInsertPost() {
        Post post = createTestPost(1, 1);
        postDao.insertPost(post);

        Post retrieved = postDao.getPostById(1, 1);
        assertNotNull("Retrieved post should not be null", retrieved);
        assertEquals("Post IDs should match", post.getPostId(), retrieved.getPostId());
    }

    /**
     * Tests retrieving all posts
     */
    @Test
    public void testGetAllPosts() {
        Post post1 = createTestPost(1, 1);
        Post post2 = createTestPost(1, 2);

        postDao.insertPost(post1);
        postDao.insertPost(post2);

        List<Post> posts = postDao.getAllPosts();
        assertEquals("Should have 2 posts", 2, posts.size());
    }

    /**
     * Tests retrieving a post by ID
     */
    @Test
    public void testGetPostById() {
        Post post = createTestPost(1, 1);
        postDao.insertPost(post);

        Post retrieved = postDao.getPostById(1, 1);
        assertNotNull("Retrieved post should not be null", retrieved);
        assertEquals("UIDs should match", post.getUid(), retrieved.getUid());
        assertEquals("Post IDs should match", post.getPostId(), retrieved.getPostId());
    }

    /**
     * Tests updating a post
     */
    @Test
    public void testUpdatePost() {
        Post post = createTestPost(1, 1);
        post.setDescription("Initial description");
        postDao.insertPost(post);

        post.setDescription("Updated description");
        postDao.updatePost(post);

        Post updated = postDao.getPostById(1, 1);
        assertEquals("Description should be updated", "Updated description",
                updated.getDescription());
    }

    /**
     * Tests deleting a post
     */
    @Test
    public void testDeletePost() {
        Post post = createTestPost(1, 1);
        postDao.insertPost(post);

        postDao.deletePost(post);

        Post deleted = postDao.getPostById(1, 1);
        assertNull("Post should be deleted", deleted);
        List<Post> posts = postDao.getAllPosts();
        assertTrue("Posts list should be empty", posts.isEmpty());
    }

    /**
     * Tests exception handling for null post insertion
     */
    @Test(expected = NullPointerException.class)
    public void testInsertNullPost() {
        Post post = null;
        postDao.insertPost(post);
    }


    /**
     * Tests exception handling for invalid foreign key
     */
    @Test(expected = SQLiteConstraintException.class)
    public void testInsertPostWithInvalidForeignKey() {
        Post post = createTestPost(999, 1);
        postDao.insertPost(post);
    }

    /**
     * Tests exception handling for updating null post
     */
    @Test(expected = NullPointerException.class)
    public void testUpdateNullPost() {
        Post post = null;
        postDao.updatePost(post);
    }

    /**
     * Tests exception handling for deleting null post
     */
    @Test(expected = NullPointerException.class)
    public void testDeleteNullPost() {
        Post post = null;
        postDao.deletePost(post);
    }

    /**
     * Helper method to create a test post with specified parameters
     *
     * @param uid The user ID for the post
     * @param postId The unique identifier for the post
     * @return A Post entity initialized with test data
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
}
