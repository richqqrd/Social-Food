package com.example.socialfood.controller.User;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.socialfood.model.database.AppDatabase;
import com.example.socialfood.model.entities.Follow;
import com.example.socialfood.model.entities.User;
import com.example.socialfood.model.handler.FollowHandler;
import com.example.socialfood.model.handler.UserHandler;
import com.example.socialfood.utils.UserManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test class for {@link com.example.socialfood.controller.User.UserController}.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private Context mockContext;

    @Mock
    private UserHandler mockUserHandler;

    @Mock
    private FollowHandler mockFollowHandler;

    @Mock
    private UserManager mockUserManager;

    private com.example.socialfood.controller.User.UserController userController;
    private AppDatabase inMemoryDatabase;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Real context for Room
        Context context = ApplicationProvider.getApplicationContext();

        // In-Memory database for testing
        inMemoryDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries() // Only for testing
                .build();



        // Initialize UserController with the real handlers and mocked UserManager
        userController = new com.example.socialfood.controller.User.UserController(context, mockUserHandler, mockFollowHandler);
        userController.setUserManager(mockUserManager);
    }

    @After
    public void tearDown() {
        if (inMemoryDatabase != null) {
            inMemoryDatabase.close();
        }
    }

    @Test
    public void testCreateUser() {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setUid(1);
        testUser.setPassword("testpassword");
        testUser.setPostsCount(4);
        testUser.setFollowersCount(4);
        testUser.setProfilImage("testImage.jpg");

        when(mockUserHandler.insert(testUser)).thenReturn(true);
        when(mockUserHandler.getUserByUsername("testuser")).thenReturn(testUser);

        int userId = userController.createUser(testUser);

        assertEquals(testUser.getUid(), userId);
        verify(mockUserHandler).insert(testUser);
        verify(mockUserHandler).getUserByUsername("testuser");
    }
    @Test
    public void testUpdateUser() {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setUid(1);
        testUser.setPassword("testpassword");
        testUser.setPostsCount(4);
        testUser.setFollowersCount(4);
        testUser.setProfilImage("testImage.jpg");

        when(mockUserHandler.update(testUser)).thenReturn(true);

        boolean result = userController.updateUser(testUser);

        assertTrue(result);
        verify(mockUserHandler).update(testUser);

    }

        @Test
        public void testDeleteUser() {
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setUid(1);
            testUser.setPassword("testpassword");
            testUser.setPostsCount(4);
            testUser.setFollowersCount(4);
            testUser.setProfilImage("testImage.jpg");

            when(mockUserHandler.delete(testUser)).thenReturn(true);

            boolean result = userController.deleteUser(testUser);

            assertTrue(result);
            verify(mockUserHandler).delete(testUser);
        }

        @Test
        public void testFollowUser() {
            User currentUser = new User();
            currentUser.setUid(1);
            currentUser.setUsername("currentuser");

            User userToFollow = new User();
            userToFollow.setUid(2);
            userToFollow.setUsername("userToFollow");

            when(mockUserManager.getCurrentUser()).thenReturn(currentUser);
            when(mockUserHandler.getUserById(2)).thenReturn(userToFollow);
            when(mockFollowHandler.insert(any(Follow.class))).thenReturn(true);
            when(mockUserHandler.update(userToFollow)).thenReturn(true);

            boolean result = userController.followUser(2);

            assertTrue(result);
            verify(mockUserManager).getCurrentUser();
            verify(mockUserHandler).getUserById(2);
            verify(mockFollowHandler).insert(any(Follow.class));
            verify(mockUserHandler).update(userToFollow);
        }

    @Test
    public void testUnfollowUser() {
        User currentUser = new User();
        currentUser.setUid(1);
        currentUser.setUsername("currentuser");

        User userToUnfollow = new User();
        userToUnfollow.setUid(2);
        userToUnfollow.setUsername("userToUnfollow");
        currentUser.setFollowersCount(1);


        when(mockUserManager.getCurrentUser()).thenReturn(currentUser);
        when(mockUserHandler.getUserById(2)).thenReturn(userToUnfollow);
        when(mockFollowHandler.delete(currentUser.getUid(), userToUnfollow.getUid())).thenReturn(true);
        when(mockUserHandler.update(userToUnfollow)).thenReturn(true);

        boolean result = userController.unfollowUser(2);

        assertTrue(result);
        verify(mockUserManager).getCurrentUser();
        verify(mockUserHandler).getUserById(2);
        verify(mockFollowHandler).delete(currentUser.getUid(), userToUnfollow.getUid());
        verify(mockUserHandler).update(userToUnfollow);
        }
    }