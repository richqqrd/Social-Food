package com.example.socialfood.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Like entity. Tests all getters, setters and validation logic.
 */
public class LikeTest {
    private Like like;

    /**
     * Sets up a Like instance before each test.
     */
    @Before
    public void setup() {
        like = new Like();
    }

    /**
     * Tests setting and getting the user ID.
     */
    @Test
    public void testUserId() {
        like.setUserId(1);
        assertEquals(1, like.getUserId());
    }

    /**
     * Tests validation of negative user ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUserId() {
        like.setUserId(-1);
    }

    /**
     * Tests setting and getting the post ID.
     */
    @Test
    public void testPostId() {
        like.setPostId(1);
        assertEquals(1, like.getPostId());
    }

    /**
     * Tests validation of negative post ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPostId() {
        like.setPostId(-1);
    }

    /**
     * Tests setting and getting the timestamp.
     */
    @Test
    public void testTimestamp() {
        long timestamp = System.currentTimeMillis();
        like.setTimestamp(timestamp);
        assertEquals(timestamp, like.getTimestamp());
    }

    /**
     * Tests validation of negative timestamp.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimestamp() {
        like.setTimestamp(-1);
    }

    /**
     * Tests equals method with same values.
     */
    @Test
    public void testEqualsWithSameValues() {
        Like like1 = createTestLike(1, 1);
        Like like2 = createTestLike(1, 1);
        assertTrue(like1.equals(like2));
    }

    /**
     * Tests equals method with different values.
     */
    @Test
    public void testEqualsWithDifferentValues() {
        Like like1 = createTestLike(1, 1);
        Like like2 = createTestLike(2, 2);
        assertFalse(like1.equals(like2));
    }

    /**
     * Tests hashCode generation.
     */
    @Test
    public void testHashCode() {
        Like like1 = createTestLike(1, 1);
        Like like2 = createTestLike(1, 1);
        assertEquals(like1.hashCode(), like2.hashCode());
    }

    /**
     * Tests toString method.
     */
    @Test
    public void testToString() {
        Like like = createTestLike(1, 1);
        String expected = "Like{userId=1, postId=1, timestamp=" + like.getTimestamp() + "}";
        assertEquals(expected, like.toString());
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