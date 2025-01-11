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
import com.example.socialfood.model.entities.Follow;
import com.example.socialfood.model.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Test class for the FollowDao interface. Tests all CRUD operations and special queries. Uses
 * Room's in-memory database for testing.
 */
public class FollowDaoTest {
    private AppDatabase database;
    private FollowDao followDao;
    private UserDao userDao;

    /**
     * Sets up the test environment before each test. Creates an in-memory database and initializes
     * the FollowDao. Also creates necessary test users to satisfy foreign key constraints.
     */
    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        followDao = database.followDao();
        userDao = database.userDao();

        // Setup required users
        User user1 = createTestUser("user1");
        User user2 = createTestUser("user2");
        userDao.insertUser(user1);
        userDao.insertUser(user2);
    }

    /**
     * Cleans up the test environment after each test. Closes the in-memory database.
     */
    @After
    public void teardown() {
        database.close();
    }

    /**
     * Tests inserting a follow relationship and basic retrieval
     */
    @Test
    public void testInsertFollow() {
        Follow follow = createTestFollow(1, 2);
        followDao.insert(follow);

        Follow retrieved = followDao.getFollow(1, 2);
        assertNotNull("Retrieved follow should not be null", retrieved);
        assertEquals("Follower IDs should match", follow.getFollowerId(),
                retrieved.getFollowerId());
    }

    /**
     * Tests retrieving all follows
     */
    @Test
    public void testGetAllFollows() {
        Follow follow1 = createTestFollow(1, 2);
        Follow follow2 = createTestFollow(2, 1);

        followDao.insert(follow1);
        followDao.insert(follow2);

        List<Follow> follows = followDao.getAll();
        assertEquals("Should have 2 follows", 2, follows.size());
    }

    /**
     * Tests getting follows by user
     */
    @Test
    public void testGetFollowsByUser() {
        User user3 = createTestUser("user3");

        userDao.insertUser(user3);

        Follow follow1 = createTestFollow(1, 2);
        Follow follow2 = createTestFollow(1, 3);
        followDao.insert(follow1);
        followDao.insert(follow2);

        List<Follow> follows = followDao.getFollowingByUser(1);
        assertEquals("Should have 2 follows", 2, follows.size());


    }

    /**
     * Tests deleting a follow relationship
     */
    @Test
    public void testDeleteFollow() {
        Follow follow = createTestFollow(1, 2);
        followDao.insert(follow);

        followDao.delete(follow);

        Follow deleted = followDao.getFollow(1, 2);
        assertNull("Follow should be deleted", deleted);
    }

    /**
     * Tests checking if user is following another
     */
    @Test
    public void testIsFollowing() {
        Follow follow = createTestFollow(1, 2);
        followDao.insert(follow);

        boolean isFollowing = followDao.isFollowing(1, 2);
        assertTrue("User 1 should be following user 2", isFollowing);

        boolean isNotFollowing = followDao.isFollowing(2, 1);
        assertFalse("User 2 should not be following user 1", isNotFollowing);
    }

    /**
     * Tests exception handling for null follow insertion
     */
    @Test(expected = NullPointerException.class)
    public void testInsertNullFollow() {
        Follow follow = null;
        followDao.insert(follow);
    }

    /**
     * Tests exception handling for duplicate follow relationship
     */
    @Test(expected = SQLiteConstraintException.class)
    public void testInsertDuplicateFollow() {
        Follow follow1 = createTestFollow(1, 2);
        Follow follow2 = createTestFollow(1, 2); // Same IDs

        followDao.insert(follow1);
        followDao.insert(follow2); // Should throw exception
    }

    /**
     * Tests exception handling for invalid foreign key
     */
    @Test(expected = SQLiteConstraintException.class)
    public void testInsertFollowWithInvalidForeignKey() {
        Follow follow = createTestFollow(999, 888); // Non-existent user IDs
        followDao.insert(follow);
    }

    /**
     * Helper method to create a test user
     */
    private User createTestUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setProfilImage("default.png");
        return user;
    }

    /**
     * Helper method to create a test follow relationship
     */
    private Follow createTestFollow(int followerId, int followedId) {
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowedId(followedId);
        follow.setTimestamp(System.currentTimeMillis());
        return follow;
    }
}