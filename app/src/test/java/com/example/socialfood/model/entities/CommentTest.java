package com.example.socialfood.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Comment entity. Tests all getters, setters and validation logic.
 */
public class CommentTest {
    private Comment comment;

    /**
     * Sets up a Comment instance before each test.
     */
    @Before
    public void setup() {
        comment = new Comment();
    }

    /**
     * Tests setting and getting the user ID.
     */
    @Test
    public void testUid() {
        comment.setUid(1);
        assertEquals(1, comment.getUid());
    }

    /**
     * Tests validation of negative user ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUid() {
        comment.setUid(-1);
    }

    /**
     * Tests setting and getting the post ID.
     */
    @Test
    public void testPostId() {
        comment.setPostId(1);
        assertEquals(1, comment.getPostId());
    }

    /**
     * Tests validation of negative post ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPostId() {
        comment.setPostId(-1);
    }

    /**
     * Tests setting and getting the comment ID.
     */
    @Test
    public void testCommentId() {
        comment.setCommentId(1);
        assertEquals(1, comment.getCommentId());
    }

    /**
     * Tests validation of negative comment ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCommentId() {
        comment.setCommentId(-1);
    }

    /**
     * Tests setting and getting the comment content.
     */
    @Test
    public void testContent() {
        comment.setContent("Test content");
        assertEquals("Test content", comment.getContent());
    }

    /**
     * Tests validation of empty content.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyContent() {
        comment.setContent("");
    }

    /**
     * Tests validation of whitespace-only content.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWhitespaceContent() {
        comment.setContent("   ");
    }

    /**
     * Tests setting and getting the timestamp.
     */
    @Test
    public void testTimestamp() {
        long timestamp = System.currentTimeMillis();
        comment.setTimestamp(timestamp);
        assertEquals(timestamp, comment.getTimestamp());
    }

    /**
     * Tests validation of negative timestamp.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimestamp() {
        comment.setTimestamp(-1);
    }

    /**
     * Tests generating the primary key.
     */
    @Test
    public void testPrimaryKey() {
        comment.setUid(1);
        comment.setPostId(2);
        comment.setCommentId(3);

        String expectedKey = "1_2_3";
        assertEquals(expectedKey, comment.getPrimaryKey());
    }

    /**
     * Tests equals method with same values.
     */
    @Test
    public void testEqualsWithSameValues() {
        Comment comment1 = createTestComment(1, 1, 1);
        Comment comment2 = createTestComment(1, 1, 1);
        assertTrue(comment1.equals(comment2));
    }

    /**
     * Tests equals method with different values.
     */
    @Test
    public void testEqualsWithDifferentValues() {
        Comment comment1 = createTestComment(1, 1, 1);
        Comment comment2 = createTestComment(2, 2, 2);
        assertFalse(comment1.equals(comment2));
    }

    /**
     * Tests hashCode generation.
     */
    @Test
    public void testHashCode() {
        Comment comment1 = createTestComment(1, 1, 1);
        Comment comment2 = createTestComment(1, 1, 1);
        assertEquals(comment1.hashCode(), comment2.hashCode());
    }

    /**
     * Helper method to create a test comment with specified parameters.
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