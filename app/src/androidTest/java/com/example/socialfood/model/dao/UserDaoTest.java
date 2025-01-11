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
import com.example.socialfood.model.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Test class for the UserDao interface. Tests all CRUD (Create, Read, Update, Delete) operations
 * and special queries. Uses Room's in-memory database for testing.
 */
public class UserDaoTest {
    private AppDatabase database;
    private UserDao userDao;

    /**
     * Sets up the test environment before each test. Creates an in-memory database and initializes
     * the UserDao.
     */
    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDao = database.userDao();
    }

    /**
     * Cleans up the test environment after each test. Closes the in-memory database.
     */
    @After
    public void teardown() {
        database.close();
    }

    /**
     * Tests inserting a user and basic retrieval
     */
    @Test
    public void testInsertUser() {
        User user = createTestUser("testuser");
        userDao.insertUser(user);

        User retrieved = userDao.getUserByUsername("testuser");
        assertNotNull("Retrieved user should not be null", retrieved);
        assertEquals("Usernames should match", user.getUsername(), retrieved.getUsername());
    }

    /**
     * Tests retrieving all users
     */
    @Test
    public void testGetAllUsers() {
        User user1 = createTestUser("testuser1");
        User user2 = createTestUser("testuser2");

        userDao.insertUser(user1);
        userDao.insertUser(user2);

        List<User> users = userDao.getAll();
        assertEquals("Should have 2 users", 2, users.size());
    }

    /**
     * Tests updating a user
     */
    @Test
    public void testUpdateUser() {
        User user = createTestUser("testuser");
        userDao.insertUser(user);

        User insertedUser = userDao.getUserByUsername("testuser");
        insertedUser.setBio("Updated bio");
        userDao.updateUsers(insertedUser);

        User updated = userDao.getUserByUsername("testuser");
        assertEquals("Bio should be updated", "Updated bio", updated.getBio());
    }

    /**
     * Tests deleting a user
     */
    @Test
    public void testDeleteUser() {
        User user = createTestUser("testuser");
        userDao.insertUser(user);

        User insertedUser = userDao.getUserByUsername("testuser");
        assertNotNull("User should be inserted", insertedUser);

        userDao.deleteUser(insertedUser);

        User deleted = userDao.getUserByUsername("testuser");
        assertNull("User should be deleted", deleted);
    }

    /**
     * Tests exception handling for null user insertion
     */
    @Test(expected = NullPointerException.class)
    public void testInsertNullUser() {
        userDao.insertUser(null);
    }

    /**
     * Tests exception handling for updating null user
     */
    @Test(expected = NullPointerException.class)
    public void testUpdateNullUser() {
        User user = null;
        userDao.updateUsers(user);
    }

    /**
     * Tests exception handling for deleting null user
     */
    @Test(expected = NullPointerException.class)
    public void testDeleteNullUser() {
        User user = null;
        userDao.deleteUser(user);
    }

    /**
     * Helper method to create a test user with specified username
     *
     * @param username The username for the test user
     * @return A User entity initialized with test data
     */
    private User createTestUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setProfilImage("default.png");
        user.setBio("Test bio");
        user.setFollowersCount(0);
        user.setPostsCount(0);
        return user;
    }
}
