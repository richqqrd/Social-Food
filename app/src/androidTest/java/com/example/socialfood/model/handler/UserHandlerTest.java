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

import com.example.socialfood.model.dao.UserDao;
import com.example.socialfood.model.database.AppDatabase;
import com.example.socialfood.model.database.DatabaseClient;
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
 * Test class for the UserHandler class. Tests all CRUD operations and special queries. Uses Mockito
 * for mocking dependencies.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserHandlerTest {
    @Mock
    private Context mockContext;

    @Mock
    private AppDatabase mockDatabase;

    @Mock
    private UserDao mockUserDao;

    private UserHandler userHandler;

    /**
     * Sets up the test environment before each test. Initializes mocks and creates the UserHandler
     * instance with mocked dependencies.
     */
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(mockDatabase.userDao()).thenReturn(mockUserDao);

        DatabaseClient mockDatabaseClient = mock(DatabaseClient.class);
        when(mockDatabaseClient.getDatabase()).thenReturn(mockDatabase);
        userHandler = new UserHandler(mockDatabaseClient);
    }

    /**
     * Tests inserting a new user into the database.
     */
    @Test
    public void testInsertUser() {
        User user = createTestUser(1);
        doNothing().when(mockUserDao).insertUser(user);

        boolean success = userHandler.insert(user);

        assertTrue("Insert should be successful", success);
        verify(mockUserDao, times(1)).insertUser(user);
    }

    /**
     * Tests retrieving all users from the database.
     */
    @Test
    public void testGetAll() {
        List<User> userList = Arrays.asList(
                createTestUser(1),
                createTestUser(2));
        when(mockUserDao.getAll()).thenReturn(userList);

        List<User> result = userHandler.getAll();

        assertEquals("Should have 2 users", 2, result.size());
        verify(mockUserDao, times(1)).getAll();
    }

    /**
     * Tests updating an existing user in the database.
     */
    @Test
    public void testUpdateUser() {
        User user = createTestUser(1);
        doNothing().when(mockUserDao).updateUsers(user);

        boolean success = userHandler.update(user);

        assertTrue("Update should be successful", success);
        verify(mockUserDao, times(1)).updateUsers(user);
    }

    /**
     * Tests deleting a user from the database.
     */
    @Test
    public void testDeleteUser() {
        User user = createTestUser(1);
        doNothing().when(mockUserDao).deleteUser(user);

        boolean success = userHandler.delete(user);

        assertTrue("Delete should be successful", success);
        verify(mockUserDao, times(1)).deleteUser(user);
    }

    /**
     * Tests retrieving a user by ID.
     */
    @Test
    public void testGetUserById() {
        User user = createTestUser(1);
        when(mockUserDao.getUserbyId(1)).thenReturn(user);

        User result = userHandler.getUserById(1);

        assertNotNull("User should not be null", result);
        assertEquals("User IDs should match", user.getUid(), result.getUid());
        verify(mockUserDao, times(1)).getUserbyId(1);
    }

    /**
     * Tests retrieving a user by username.
     */
    @Test
    public void testGetUserByUsername() {
        User user = createTestUser(1);
        when(mockUserDao.getUserByUsername("testuser")).thenReturn(user);

        User result = userHandler.getUserByUsername("testuser");

        assertNotNull("User should not be null", result);
        assertEquals("Usernames should match", user.getUsername(), result.getUsername());
        verify(mockUserDao, times(1)).getUserByUsername("testuser");
    }

    /**
     * Tests error handling when trying to insert a null user.
     */
    @Test
    public void testInsertNullUser() {
        boolean success = userHandler.insert(null);
        assertFalse("Inserting null user should fail", success);
        verify(mockUserDao, never()).insertUser(any());
    }

    /**
     * Tests error handling when trying to update a null user.
     */
    @Test
    public void testUpdateNullUser() {
        boolean success = userHandler.update(null);
        assertFalse("Updating null user should fail", success);
        verify(mockUserDao, never()).updateUsers(any());
    }

    /**
     * Tests error handling when trying to delete a null user.
     */
    @Test
    public void testDeleteNullUser() {
        boolean success = userHandler.delete(null);
        assertFalse("Deleting null user should fail", success);
        verify(mockUserDao, never()).deleteUser(any());
    }

    /**
     * Tests exception handling for duplicate username.
     */
    @Test
    public void testInsertDuplicateUser() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(2);
        user2.setUsername("testuser"); // Same username as user1

        doNothing().when(mockUserDao).insertUser(user1);
        doThrow(new SQLiteConstraintException()).when(mockUserDao).insertUser(user2);

        assertTrue("First insert should succeed", userHandler.insert(user1));
        assertFalse("Duplicate insert should fail", userHandler.insert(user2));
    }

    /**
     * Tests retrieving a user with invalid ID.
     */
    @Test
    public void testGetUserByInvalidId() {
        User result = userHandler.getUserById(-1);
        assertEquals("Invalid ID should return null", null, result);
        verify(mockUserDao, never()).getUserbyId(any(Integer.class));
    }

    /**
     * Tests retrieving a user with invalid username.
     */
    @Test
    public void testGetUserByInvalidUsername() {
        User result = userHandler.getUserByUsername("");
        assertEquals("Invalid username should return null", null, result);
        verify(mockUserDao, never()).getUserByUsername(any(String.class));
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