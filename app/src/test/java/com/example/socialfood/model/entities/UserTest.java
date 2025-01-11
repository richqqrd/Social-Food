package com.example.socialfood.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for User entity. Tests all getters, setters and validation logic.
 */
public class UserTest {
    private User user;

    /**
     * Sets up a User instance before each test.
     */
    @Before
    public void setup() {
        user = new User();
    }

    /**
     * Tests setting and getting the user ID.
     */
    @Test
    public void testUid() {
        user.setUid(1);
        assertEquals(1, user.getUid());
    }

    /**
     * Tests validation of negative user ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUid() {
        user.setUid(-1);
    }

    /**
     * Tests setting and getting the username.
     */
    @Test
    public void testUsername() {
        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername());
    }

    /**
     * Tests validation of empty username.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyUsername() {
        user.setUsername("");
    }

    /**
     * Tests validation of whitespace username.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWhitespaceUsername() {
        user.setUsername("   ");
    }

    /**
     * Tests setting and getting the profile image.
     */
    @Test
    public void testProfileImage() {
        user.setProfilImage("test.jpg");
        assertEquals("test.jpg", user.getProfilImage());
    }

    /**
     * Tests validation of empty profile image.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyProfileImage() {
        user.setProfilImage("");
    }

    /**
     * Tests validation of whitespace profile image.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWhitespaceProfileImage() {
        user.setProfilImage("   ");
    }

    /**
     * Tests setting and getting the bio.
     */
    @Test
    public void testBio() {
        user.setBio("Test bio");
        assertEquals("Test bio", user.getBio());
    }

    /**
     * Tests setting and getting the followers count.
     */
    @Test
    public void testFollowersCount() {
        user.setFollowersCount(5);
        assertEquals(5, user.getFollowersCount());
    }

    /**
     * Tests validation of negative followers count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFollowersCount() {
        user.setFollowersCount(-1);
    }

    /**
     * Tests setting and getting the posts count.
     */
    @Test
    public void testPostsCount() {
        user.setPostsCount(5);
        assertEquals(5, user.getPostsCount());
    }

    /**
     * Tests validation of negative posts count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPostsCount() {
        user.setPostsCount(-1);
    }

    /**
     * Tests setting and getting the password.
     */
    @Test
    public void testPassword() {
        user.setPassword("password123");
        assertEquals("password123", user.getPassword());
    }

    /**
     * Tests validation of empty password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPassword() {
        user.setPassword("");
    }

    /**
     * Tests validation of whitespace password.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWhitespacePassword() {
        user.setPassword("   ");
    }

    /**
     * Tests equals method with same values.
     */
    @Test
    public void testEqualsWithSameValues() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(1);
        assertTrue(user1.equals(user2));
    }

    /**
     * Tests equals method with different values.
     */
    @Test
    public void testEqualsWithDifferentValues() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(2);
        assertFalse(user1.equals(user2));
    }

    /**
     * Tests hashCode generation.
     */
    @Test
    public void testHashCode() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(1);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    /**
     * Tests toString method.
     */
    @Test
    public void testToString() {
        User user = createTestUser(1);
        String expected = "User{uid=1, username='testuser', profilImage='default.png', " +
                "bio='Test bio', followersCount=0, postsCount=0, password='password'}";
        assertEquals(expected, user.toString());
    }

    /**
     * Helper method to create a test user with specified parameters.
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
        user.setBio("Test bio");
        user.setFollowersCount(0);
        user.setPostsCount(0);
        return user;
    }
}