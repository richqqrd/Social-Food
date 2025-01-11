package com.example.socialfood.model.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import com.example.socialfood.model.dao.LikeDao;
import com.example.socialfood.model.dao.PostDao;
import com.example.socialfood.model.database.AppDatabase;
import com.example.socialfood.model.database.DatabaseClient;
import com.example.socialfood.model.entities.Post;
import com.example.socialfood.model.entities.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the PostHandler class. Tests all CRUD operations and special queries. Uses Mockito
 * for mocking dependencies.
 */
@RunWith(MockitoJUnitRunner.class)
public class PostHandlerTest {
    @Mock
    private Context mockContext;

    @Mock
    private AppDatabase mockDatabase;

    @Mock
    private PostDao mockPostDao;

    @Mock
    private LikeDao mockLikeDao;

    private PostHandler postHandler;

    /**
     * Sets up the test environment before each test. Initializes mocks and creates the PostHandler
     * instance with mocked dependencies.
     */
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(mockDatabase.postDao()).thenReturn(mockPostDao);


        DatabaseClient mockDatabaseClient = mock(DatabaseClient.class);
        when(mockDatabaseClient.getDatabase()).thenReturn(mockDatabase);

        postHandler = new PostHandler(mockDatabaseClient);
    }

    /**
     * Tests inserting a new post into the database.
     */
    @Test
    public void testInsertPost() {
        Post post = createTestPost(1, 1);
        doNothing().when(mockPostDao).insertPost(post);

        boolean success = postHandler.insert(post);

        assertTrue("Insert should be successful", success);
        verify(mockPostDao, times(1)).insertPost(post);
    }

    /**
     * Tests retrieving all posts from the database.
     */
    @Test
    public void testGetAll() {
        List<Post> postList = Arrays.asList(
                createTestPost(1, 1),
                createTestPost(1, 2));
        when(mockPostDao.getAllPosts()).thenReturn(postList);

        List<Post> result = postHandler.getAll();

        assertEquals("Should have 2 posts", 2, result.size());
        verify(mockPostDao, times(1)).getAllPosts();
    }

    /**
     * Tests updating an existing post in the database.
     */
    @Test
    public void testUpdatePost() {
        Post post = createTestPost(1, 1);
        doNothing().when(mockPostDao).updatePost(post);

        boolean success = postHandler.update(post);

        assertTrue("Update should be successful", success);
        verify(mockPostDao, times(1)).updatePost(post);
    }

    /**
     * Tests deleting a post from the database.
     */
    @Test
    public void testDeletePost() {
        Post post = createTestPost(1, 1);
        doNothing().when(mockPostDao).deletePost(post);

        boolean success = postHandler.delete(post);

        assertTrue("Delete should be successful", success);
        verify(mockPostDao, times(1)).deletePost(post);
    }

    /**
     * Tests retrieving a specific post by its composite key.
     */
    @Test
    public void testGetPostById() {
        Post post = createTestPost(1, 1);
        when(mockPostDao.getPostById(1, 1)).thenReturn(post);

        Post result = postHandler.getPostById(1, 1);

        assertNotNull("Post should not be null", result);
        assertEquals("Post IDs should match", post.getPostId(), result.getPostId());
        verify(mockPostDao, times(1)).getPostById(1, 1);
    }

    /**
     * Tests retrieving all posts for a specific user.
     */
    @Test
    public void testGetPostByUser() {
        List<Post> postList = Arrays.asList(
                createTestPost(1, 1),
                createTestPost(1, 2));
        when(mockPostDao.getPostByUser(1)).thenReturn(postList);

        List<Post> result = postHandler.getPostByUser(1);

        assertEquals("User should have 2 posts", 2, result.size());
        verify(mockPostDao, times(1)).getPostByUser(1);
    }


    /**
     * Tests retrieving all posts with additional information.
     */
    @Test
    public void testGetAllPosts() {
        List<Post> postList = Arrays.asList(
                createTestPost(1, 1),
                createTestPost(1, 2));
        when(mockPostDao.getAllPosts()).thenReturn(postList);

        List<Post> result = postHandler.getAllPosts();

        assertEquals("Should have 2 posts", 2, result.size());
        verify(mockPostDao, times(1)).getAllPosts();
    }

    /**
     * Tests error handling when trying to insert a null post.
     */
    @Test
    public void testInsertNullPost() {
        boolean success = postHandler.insert(null);
        assertFalse("Inserting null post should fail", success);
        verify(mockPostDao, never()).insertPost(any());
    }

    /**
     * Tests error handling when trying to update a null post.
     */
    @Test
    public void testUpdateNullPost() {
        boolean success = postHandler.update(null);
        assertFalse("Updating null post should fail", success);
        verify(mockPostDao, never()).updatePost(any());
    }

    /**
     * Tests error handling when trying to delete a null post.
     */
    @Test
    public void testDeleteNullPost() {
        boolean success = postHandler.delete(null);
        assertFalse("Deleting null post should fail", success);
        verify(mockPostDao, never()).deletePost(any());
    }

    /**
     * Tests exception handling for invalid foreign key.
     */
    @Test
    public void testInsertPostWithInvalidForeignKey() {
        Post post = createTestPost(999, 1); // Non-existent user ID
        doThrow(new SQLiteConstraintException()).when(mockPostDao).insertPost(post);

        boolean success = postHandler.insert(post);
        assertFalse("Insert with invalid foreign key should fail", success);
    }

    /**
     * Helper method to create a test post with specified parameters.
     *
     * @param uid The user ID for the post
     * @param postId The unique identifier for the post
     * @return A Post entity initialized with test data
     */
    private Post createTestPost(int uid, int postId) {
        Post post = new Post();
        post.setUid(uid);
        post.setPostId(postId);
        post.setDescription("Test post");
        post.setImageUrl("default.png");
        post.setTimestamp(System.currentTimeMillis());
        post.setCommentCount(0);
        post.setLatitude(0.0);
        post.setLongitude(0.0);
        return post;
    }

    /**
     * Helper method to create a test user.
     *
     * @param uid The user ID
     * @return A User entity initialized with test data
     */
    private User createTestUser(int uid) {
        User user = new User();
        user.setUid(uid);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setProfilImage("default.png");
        return user;
    }
}