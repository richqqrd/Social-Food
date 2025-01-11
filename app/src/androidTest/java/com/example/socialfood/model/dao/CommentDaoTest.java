package com.example.socialfood.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.socialfood.model.database.AppDatabase;
import com.example.socialfood.model.entities.Comment;
import com.example.socialfood.model.entities.Post;
import com.example.socialfood.model.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Test class for the CommentDao interface. Tests all CRUD (Create, Read, Update, Delete) operations
 * and special queries. Uses Room's in-memory database for testing.
 */
public class CommentDaoTest {
    private AppDatabase database;
    private CommentDao commentDao;
    private UserDao userDao;
    private PostDao postDao;

    /**
     * Sets up the test environment before each test. Creates an in-memory database and initializes
     * the CommentDao. Also creates necessary test data (user and post) to satisfy foreign key
     * constraints.
     */
    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        commentDao = database.commentDao();
        userDao = database.userDao();
        postDao = database.postDao();

        // Setup required entities
        User user = new User();
        user.setUid(1);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setProfilImage("default.png");
        userDao.insertUser(user);

        Post post = new Post();
        post.setUid(1);
        post.setPostId(1);
        post.setImageUrl("default.png");
        post.setCommentCount(0);
        post.setTimestamp(System.currentTimeMillis());
        post.setLatitude(0.0);
        post.setLongitude(0.0);
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
     * Tests inserting a comment and basic retrieval
     */
    @Test
    public void testInsertComment() {
        Comment comment = createTestComment(1, 1, 1);
        commentDao.insertComment(comment);

        Comment retrieved = commentDao.getCommentById(1, 1, 1);
        assertNotNull("Retrieved comment should not be null", retrieved);
        assertEquals("Comment content should match", comment.getContent(), retrieved.getContent());
    }

    /**
     * Tests retrieving all comments
     */
    @Test
    public void testGetAll() {
        Comment comment1 = createTestComment(1, 1, 1);
        Comment comment2 = createTestComment(1, 1, 2);

        commentDao.insertComment(comment1);
        commentDao.insertComment(comment2);

        List<Comment> comments = commentDao.getAll();
        assertEquals("Should have 2 comments", 2, comments.size());
    }

    /**
     * Tests retrieving comments for a specific post
     */
    @Test
    public void testGetCommentsByPostId() {
        Post post2 = new Post();
        post2.setUid(1);
        post2.setPostId(2);
        post2.setImageUrl("default.png");
        post2.setCommentCount(0);
        post2.setTimestamp(System.currentTimeMillis());
        post2.setLatitude(0.0);
        post2.setLongitude(0.0);
        postDao.insertPost(post2);

        Comment comment1 = createTestComment(1, 1, 1);
        Comment comment2 = createTestComment(1, 1, 2);
        Comment comment3 = createTestComment(1, 2, 1);

        commentDao.insertComment(comment1);
        commentDao.insertComment(comment2);
        commentDao.insertComment(comment3);

        List<Comment> commentsForPost1 = commentDao.getCommentsByPostId(1);
        assertEquals("Post 1 should have 2 comments", 2, commentsForPost1.size());
    }

    /**
     * Tests updating a comment
     */
    @Test
    public void testUpdateComment() {
        Comment comment = createTestComment(1, 1, 1);
        commentDao.insertComment(comment);

        comment.setContent("Updated content");
        commentDao.updateComment(comment);

        Comment updated = commentDao.getCommentById(1, 1, 1);
        assertEquals("Content should be updated", "Updated content", updated.getContent());
    }

    /**
     * Tests deleting a comment
     */
    @Test
    public void testDeleteComment() {
        Comment comment = createTestComment(1, 1, 1);
        commentDao.insertComment(comment);

        commentDao.deleteComment(comment);

        Comment deleted = commentDao.getCommentById(1, 1, 1);
        assertNull("Comment should be deleted", deleted);
    }

    /**
     * Tests exception handling for null comment insertion
     */
    @Test(expected = NullPointerException.class)
    public void testInsertNullComment() {
        Comment comment = null;
        commentDao.insertComment(comment);
    }

    /**
     * Tests exception handling for duplicate comment ID
     */
    @Test(expected = SQLiteConstraintException.class)
    public void testInsertDuplicateComment() {
        Comment comment1 = createTestComment(1, 1, 1);
        Comment comment2 = createTestComment(1, 1, 1); // Same IDs

        commentDao.insertComment(comment1);
        commentDao.insertComment(comment2); // Should throw exception
    }

    /**
     * Tests exception handling for invalid foreign key
     */
    @Test(expected = SQLiteConstraintException.class)
    public void testInsertCommentWithInvalidForeignKey() {
        Comment comment = createTestComment(999, 999, 1); // Non-existent user and post IDs
        commentDao.insertComment(comment);
    }

    /**
     * Tests exception handling for updating null comment
     */
    @Test(expected = NullPointerException.class)
    public void testUpdateNullComment() {
        Comment comment = null;
        commentDao.updateComment(comment);
    }

    /**
     * Tests exception handling for deleting null comment
     */
    @Test(expected = NullPointerException.class)
    public void testDeleteNullComment() {
        Comment comment = null;
        commentDao.deleteComment(comment);
    }

    /**
     * Helper method to create a test comment with specified parameters.
     *
     * @param uid The user ID for the comment
     * @param postId The post ID the comment belongs to
     * @param commentId The unique identifier for the comment
     * @return A Comment entity initialized with test data
     */
    private Comment createTestComment(int uid, int postId, int commentId) {
        Comment comment = new Comment();
        comment.setUid(uid);
        comment.setPostId(postId);
        comment.setCommentId(commentId);
        comment.setContent("Test content");
        comment.setTimestamp(System.currentTimeMillis());
        return comment;
    }
}
