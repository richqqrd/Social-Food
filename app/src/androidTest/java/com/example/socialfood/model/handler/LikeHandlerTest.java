package com.example.socialfood.model.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import com.example.socialfood.model.database.AppDatabase;
import com.example.socialfood.model.database.DatabaseClient;
import com.example.socialfood.model.entities.Like;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the LikeHandler class. Tests all CRUD operations and special queries. Uses Mockito
 * for mocking dependencies.
 */
@RunWith(MockitoJUnitRunner.class)
public class LikeHandlerTest {
    @Mock
    private Context mockContext;

    @Mock
    private AppDatabase mockDatabase;

    @Mock
    private LikeDao mockLikeDao;

    private LikeHandler likeHandler;

    /**
     * Sets up the test environment before each test. Initializes mocks and creates the LikeHandler
     * instance with mocked dependencies.
     */
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(mockDatabase.likeDao()).thenReturn(mockLikeDao);

        DatabaseClient mockDatabaseClient = mock(DatabaseClient.class);
        when(mockDatabaseClient.getDatabase()).thenReturn(mockDatabase);
        likeHandler = new LikeHandler(mockDatabaseClient);
    }

    /**
     * Tests inserting a new like into the database.
     */
    @Test
    public void testInsertLike() {
        Like like = createTestLike(1, 1);
        doNothing().when(mockLikeDao).insert(like);

        boolean success = likeHandler.insert(like);

        assertTrue("Insert should be successful", success);
        verify(mockLikeDao, times(1)).insert(like);
    }

    /**
     * Tests retrieving all likes from the database.
     */
    @Test
    public void testGetAll() {
        List<Like> likeList = Arrays.asList(
                createTestLike(1, 1),
                createTestLike(2, 1));
        when(mockLikeDao.getAll()).thenReturn(likeList);

        List<Like> result = likeHandler.getAll();

        assertEquals("Should have 2 likes", 2, result.size());
        verify(mockLikeDao, times(1)).getAll();
    }

    /**
     * Tests deleting a like from the database.
     */
    @Test
    public void testDeleteLike() {
        Like like = createTestLike(1, 1);
        doNothing().when(mockLikeDao).delete(like);

        boolean success = likeHandler.delete(like);

        assertTrue("Delete should be successful", success);
        verify(mockLikeDao, times(1)).delete(like);
    }

    /**
     * Tests toggling a like (from unliked to liked).
     */
    @Test
    public void testToggleLikeAdd() {
        when(mockLikeDao.isLikedByUser(1, 1)).thenReturn(false);
        doNothing().when(mockLikeDao).insert(any(Like.class));

        boolean result = likeHandler.toggleLike(1, 1);

        assertTrue("Toggle should result in like being added", result);
        verify(mockLikeDao, times(1)).isLikedByUser(1, 1);
        verify(mockLikeDao, times(1)).insert(any(Like.class));
    }

    /**
     * Tests toggling a like (from liked to unliked).
     */
    @Test
    public void testToggleLikeRemove() {
        when(mockLikeDao.isLikedByUser(1, 1)).thenReturn(true);
        doNothing().when(mockLikeDao).delete(any(Like.class));

        boolean result = likeHandler.toggleLike(1, 1);

        assertFalse("Toggle should result in like being removed", result);
        verify(mockLikeDao, times(1)).isLikedByUser(1, 1);
        verify(mockLikeDao, times(1)).delete(any(Like.class));
    }

    /**
     * Tests checking if a post is liked by a user.
     */
    @Test
    public void testIsLikedByUser() {
        when(mockLikeDao.isLikedByUser(1, 1)).thenReturn(true);

        boolean result = likeHandler.isLikedByUser(1, 1);

        assertTrue("Post should be liked by user", result);
        verify(mockLikeDao, times(1)).isLikedByUser(1, 1);
    }

    /**
     * Tests getting like count for a post.
     */
    @Test
    public void testGetLikeCount() {
        when(mockLikeDao.getLikeCount(1)).thenReturn(5);

        int count = likeHandler.getLikeCount(1);

        assertEquals("Post should have 5 likes", 5, count);
        verify(mockLikeDao, times(1)).getLikeCount(1);
    }

    /**
     * Tests error handling when trying to insert a null like.
     */
    @Test
    public void testInsertNullLike() {
        boolean success = likeHandler.insert(null);
        assertFalse("Inserting null like should fail", success);
        verify(mockLikeDao, never()).insert(any());
    }

    /**
     * Tests error handling when trying to delete a null like.
     */
    @Test
    public void testDeleteNullLike() {
        boolean success = likeHandler.delete(null);
        assertFalse("Deleting null like should fail", success);
        verify(mockLikeDao, never()).delete(any());
    }

    /**
     * Tests error handling for invalid user/post IDs in isLikedByUser.
     */
    @Test
    public void testIsLikedByUserInvalidIds() {
        boolean result = likeHandler.isLikedByUser(-1, -1);
        assertFalse("Invalid IDs should return false", result);
        verify(mockLikeDao, never()).isLikedByUser(any(Integer.class), any(Integer.class));
    }

    /**
     * Tests error handling for invalid post ID in getLikeCount.
     */
    @Test
    public void testGetLikeCountInvalidId() {
        int count = likeHandler.getLikeCount(-1);
        assertEquals("Invalid post ID should return 0 likes", 0, count);
        verify(mockLikeDao, never()).getLikeCount(any(Integer.class));
    }

    /**
     * Tests exception handling for invalid foreign key
     */
    @Test
    public void testInsertLikeWithInvalidForeignKey() {
        Like like = createTestLike(999, 888); // Non-existent user/post IDs
        doThrow(new SQLiteConstraintException()).when(mockLikeDao).insert(like);

        boolean success = likeHandler.insert(like);
        assertFalse("Insert with invalid foreign key should fail", success);
    }

    /**
     * Helper method to create a test like with specified parameters.
     *
     * @param userId The ID of the user
     * @param postId The ID of the post
     * @return A Like entity initialized with test data
     */
    private Like createTestLike(int userId, int postId) {
        Like like = new Like();
        like.setUserId(userId);
        like.setPostId(postId);
        like.setTimestamp(System.currentTimeMillis());
        return like;
    }
}