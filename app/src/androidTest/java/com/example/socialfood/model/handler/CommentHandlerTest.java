package com.example.socialfood.model.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import android.content.Context;

import com.example.socialfood.model.dao.CommentDao;
import com.example.socialfood.model.database.AppDatabase;
import com.example.socialfood.model.database.DatabaseClient;
import com.example.socialfood.model.entities.Comment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

/**
 * Unit test for CommentHandler using Mockito to mock dependencies.
 */
public class CommentHandlerTest {

    @Mock
    private Context mockContext;
    @Mock
    private AppDatabase mockDatabase;
    @Mock
    private CommentDao mockCommentDao;

    private CommentHandler commentHandler;

    /**
     * Sets up the test environment before each test case.
     * Initializes Mockito mocks and injects them into the CommentHandler.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockDatabase.commentDao()).thenReturn(mockCommentDao);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);

        DatabaseClient mockDatabaseClient = mock(DatabaseClient.class);
        when(mockDatabaseClient.getDatabase()).thenReturn(mockDatabase);

        commentHandler = new CommentHandler(mockDatabaseClient);
    }

    @Test
    public void testInsertComment() {
        Comment mockComment = createTestComment(1, 1, 1);
        doNothing().when(mockCommentDao).insertComment(mockComment);

        boolean result = commentHandler.insert(mockComment);

        assertTrue(result);
        verify(mockCommentDao, times(1)).insertComment(mockComment);
    }

    /**
     * Tests inserting a valid comment into the database.
     * Verifies that the DAO's insert method is called once.
     */
    @Test
    public void testInsertNullComment() {
        boolean result = commentHandler.insert(null);

        assertFalse(result);
        verify(mockCommentDao, never()).insertComment(any());
    }

    /**
     * Tests retrieving all comments from the database.
     * Verifies that the DAO's getAll method is called and the correct result is returned.
     */
    @Test
    public void testGetAll() {
        List<Comment> mockComments = Arrays.asList(
                createTestComment(1, 1, 1),
                createTestComment(1, 1, 2)
        );
        when(mockCommentDao.getAll()).thenReturn(mockComments);

        List<Comment> result = commentHandler.getAll();

        assertEquals(2, result.size());
        verify(mockCommentDao, times(1)).getAll();
    }

    /**
     * Tests updating a valid comment.
     * Verifies that the DAO's update method is called once.
     */
    @Test
    public void testUpdateComment() {
        Comment mockComment = createTestComment(1, 1, 1);
        doNothing().when(mockCommentDao).updateComment(mockComment);

        boolean result = commentHandler.update(mockComment);

        assertTrue(result);
        verify(mockCommentDao, times(1)).updateComment(mockComment);
    }

    /**
     * Tests updating a null comment.
     * Verifies that the operation fails and the DAO's update method is never called.
     */
    @Test
    public void testUpdateNullComment() {
        boolean result = commentHandler.update(null);

        assertFalse(result);
        verify(mockCommentDao, never()).updateComment(any());
    }

    /**
     * Tests deleting a valid comment.
     * Verifies that the DAO's delete method is called once.
     */
    @Test
    public void testDeleteComment() {
        Comment mockComment = createTestComment(1, 1, 1);
        doNothing().when(mockCommentDao).deleteComment(mockComment);

        boolean result = commentHandler.delete(mockComment);

        assertTrue(result);
        verify(mockCommentDao, times(1)).deleteComment(mockComment);
    }

    /**
     * Tests deleting a null comment.
     * Verifies that the operation fails and the DAO's delete method is never called.
     */
    @Test
    public void testDeleteNullComment() {
        boolean result = commentHandler.delete(null);

        assertFalse(result);
        verify(mockCommentDao, never()).deleteComment(any());
    }

    /**
     * Tests retrieving a specific comment by its composite key (UID, PostID, CommentID).
     * Verifies that the DAO's getCommentById method is called with the correct arguments.
     */
    @Test
    public void testGetCommentById() {
        Comment mockComment = createTestComment(1, 1, 1);
        when(mockCommentDao.getCommentById(1, 1, 1)).thenReturn(mockComment);

        Comment result = commentHandler.getCommentById(1, 1, 1);

        assertNotNull(result);
        assertEquals(mockComment.getCommentId(), result.getCommentId());
        verify(mockCommentDao, times(1)).getCommentById(1, 1, 1);
    }

    /**
     * Tests retrieving comments by a specific post ID.
     * Verifies that the DAO's getCommentsByPostId method is called with the correct post ID.
     */
    @Test
    public void testGetCommentsByPostId() {
        List<Comment> mockComments = Arrays.asList(
                createTestComment(1, 1, 1),
                createTestComment(1, 1, 2)
        );
        when(mockCommentDao.getCommentsByPostId(1)).thenReturn(mockComments);

        List<Comment> result = commentHandler.getCommentsByPostId(1);

        assertEquals(2, result.size());
        verify(mockCommentDao, times(1)).getCommentsByPostId(1);
    }

    /**
     * Helper method to create a mock Comment object for testing purposes.
     *
     * @param uid The user ID associated with the comment
     * @param postId The post ID the comment belongs to
     * @param commentId The unique ID of the comment
     * @return A Comment object populated with test data
     */
    private Comment createTestComment(int uid, int postId, int commentId) {
        Comment comment = new Comment();
        comment.setUid(uid);
        comment.setPostId(postId);
        comment.setCommentId(commentId);
        comment.setContent("Test Content");
        comment.setTimestamp(System.currentTimeMillis());
        return comment;
    }
}
