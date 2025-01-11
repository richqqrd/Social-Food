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

import com.example.socialfood.model.dao.FollowDao;
import com.example.socialfood.model.database.AppDatabase;
import com.example.socialfood.model.database.DatabaseClient;
import com.example.socialfood.model.entities.Follow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the FollowHandler class. Tests all CRUD operations and special queries. Uses
 * Mockito for mocking dependencies.
 */
@RunWith(MockitoJUnitRunner.class)
public class FollowHandlerTest {
    @Mock
    private Context mockContext;

    @Mock
    private AppDatabase mockDatabase;

    @Mock
    private FollowDao mockFollowDao;

    private FollowHandler followHandler;

    /**
     * Sets up the test environment before each test. Initializes mocks and creates the
     * FollowHandler instance with mocked dependencies.
     */
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(mockDatabase.followDao()).thenReturn(mockFollowDao);

        DatabaseClient mockDatabaseClient = mock(DatabaseClient.class);
        when(mockDatabaseClient.getDatabase()).thenReturn(mockDatabase);
        followHandler = new FollowHandler(mockDatabaseClient);
    }

    /**
     * Tests if a follow relationship exists between users
     */
    @Test
    public void testExists() {
        when(mockFollowDao.isFollowing(1, 2)).thenReturn(true);

        boolean exists = followHandler.exists(1, 2);

        assertTrue("Follow relationship should exist", exists);
        verify(mockFollowDao, times(1)).isFollowing(1, 2);
    }

    /**
     * Tests getting all users that a specific user is following
     */
    @Test
    public void testGetFollowingByUser() {
        List<Follow> followList = Arrays.asList(
                createTestFollow(1, 2),
                createTestFollow(1, 3));
        when(mockFollowDao.getFollowingByUser(1)).thenReturn(followList);

        List<Follow> result = followHandler.getFollowingByUser(1);

        assertEquals("User should be following 2 others", 2, result.size());
        verify(mockFollowDao, times(1)).getFollowingByUser(1);
    }

    /**
     * Tests getting all followers of a specific user
     */
    @Test
    public void testGetFollowersByUser() {
        List<Follow> followList = Arrays.asList(
                createTestFollow(2, 1),
                createTestFollow(3, 1));
        when(mockFollowDao.getFollowersByUser(1)).thenReturn(followList);

        List<Follow> result = followHandler.getFollowersByUser(1);

        assertEquals("User should have 2 followers", 2, result.size());
        verify(mockFollowDao, times(1)).getFollowersByUser(1);
    }

    /**
     * Tests inserting a new follow relationship into the database.
     */
    @Test
    public void testInsertFollow() {
        Follow follow = createTestFollow(1, 2);
        doNothing().when(mockFollowDao).insert(follow);

        boolean success = followHandler.insert(follow);

        assertTrue("Insert should be successful", success);
        verify(mockFollowDao, times(1)).insert(follow);
    }

    /**
     * Tests retrieving all follows from the database.
     */
    @Test
    public void testGetAll() {
        List<Follow> followList = Arrays.asList(
                createTestFollow(1, 2),
                createTestFollow(2, 3));
        when(mockFollowDao.getAll()).thenReturn(followList);

        List<Follow> result = followHandler.getAll();

        assertEquals("Should have 2 follows", 2, result.size());
        verify(mockFollowDao, times(1)).getAll();
    }

    /**
     * Tests deleting a follow relationship from the database.
     */
    @Test
    public void testDeleteFollow() {
        Follow follow = createTestFollow(1, 2);
        doNothing().when(mockFollowDao).delete(follow);

        boolean success = followHandler.delete(follow);

        assertTrue("Delete should be successful", success);
        verify(mockFollowDao, times(1)).delete(follow);
    }

    /**
     * Tests error handling when trying to insert a null follow.
     */
    @Test
    public void testInsertNullFollow() {
        boolean success = followHandler.insert(null);
        assertFalse("Inserting null follow should fail", success);
        verify(mockFollowDao, never()).insert(any());
    }

    /**
     * Tests error handling when trying to delete a null follow.
     */
    @Test
    public void testDeleteNullFollow() {
        boolean success = followHandler.delete(null);
        assertFalse("Deleting null follow should fail", success);
        verify(mockFollowDao, never()).delete(any());
    }

    /**
     * Tests exception handling for invalid foreign key
     */
    @Test
    public void testInsertFollowWithInvalidForeignKey() {
        Follow follow = createTestFollow(999, 888); // Non-existent user IDs
        doThrow(new SQLiteConstraintException()).when(mockFollowDao).insert(follow);

        boolean success = followHandler.insert(follow);
        assertFalse("Insert with invalid foreign key should fail", success);
    }

    /**
     * Helper method to create a test follow relationship with specified parameters.
     *
     * @param followerId The ID of the follower
     * @param followedId The ID of the user being followed
     * @return A Follow entity initialized with test data
     */
    private Follow createTestFollow(int followerId, int followedId) {
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowedId(followedId);
        follow.setTimestamp(System.currentTimeMillis());
        return follow;
    }
}