package com.example.socialfood.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Follow entity. Tests all getters, setters and validation logic.
 */
public class FollowTest {
    private Follow follow;

    /**
     * Sets up a Follow instance before each test.
     */
    @Before
    public void setup() {
        follow = new Follow();
    }

    /**
     * Tests setting and getting the follower ID.
     */
    @Test
    public void testFollowerId() {
        follow.setFollowerId(1);
        assertEquals(1, follow.getFollowerId());
    }

    /**
     * Tests validation of negative follower ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFollowerId() {
        follow.setFollowerId(-1);
    }

    /**
     * Tests setting and getting the followed ID.
     */
    @Test
    public void testFollowedId() {
        follow.setFollowedId(1);
        assertEquals(1, follow.getFollowedId());
    }

    /**
     * Tests validation of negative followed ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFollowedId() {
        follow.setFollowedId(-1);
    }

    /**
     * Tests setting and getting the timestamp.
     */
    @Test
    public void testTimestamp() {
        long timestamp = System.currentTimeMillis();
        follow.setTimestamp(timestamp);
        assertEquals(timestamp, follow.getTimestamp());
    }

    /**
     * Tests validation of negative timestamp.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimestamp() {
        follow.setTimestamp(-1);
    }

    /**
     * Tests equals method with same values.
     */
    @Test
    public void testEqualsWithSameValues() {
        Follow follow1 = createTestFollow(1, 2);
        Follow follow2 = createTestFollow(1, 2);
        assertTrue(follow1.equals(follow2));
    }

    /**
     * Tests equals method with different values.
     */
    @Test
    public void testEqualsWithDifferentValues() {
        Follow follow1 = createTestFollow(1, 2);
        Follow follow2 = createTestFollow(2, 3);
        assertFalse(follow1.equals(follow2));
    }

    /**
     * Tests hashCode generation.
     */
    @Test
    public void testHashCode() {
        Follow follow1 = createTestFollow(1, 2);
        Follow follow2 = createTestFollow(1, 2);
        assertEquals(follow1.hashCode(), follow2.hashCode());
    }

    /**
     * Tests toString method.
     */
    @Test
    public void testToString() {
        Follow follow = createTestFollow(1, 2);
        String expected = "Follow{followerId=1, followedId=2, timestamp=" + follow.getTimestamp()
                + "}";
        assertEquals(expected, follow.toString());
    }

    /**
     * Helper method to create a test follow relationship with specified parameters.
     */
    private Follow createTestFollow(int followerId, int followedId) {
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowedId(followedId);
        follow.setTimestamp(System.currentTimeMillis());
        return follow;
    }
}