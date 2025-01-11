package com.example.socialfood.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.socialfood.model.entities.User;
import com.example.socialfood.model.handler.UserHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for UserManager. Tests basic user session management functionality.
 */
@RunWith(AndroidJUnit4.class)
public class UserManagerTest {

    private UserManager userManager;
    private Context context;

    /**
     * Sets up test environment before each test.
     * Initializes UserManager instance and resets singleton state.
     */
    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        UserManager.setInstance(null); // Reset singleton
        userManager = UserManager.getInstance(context);
    }

    /**
     * Tests user login functionality.
     */
    @Test
    public void testLoginUser() {
        User testUser = createTestUser(1);

        userManager.loginUser(testUser);

        assertTrue(userManager.isLoggedIn());
        assertEquals(testUser, userManager.getCurrentUser());
        assertEquals(1, userManager.getCurrentUserId());
    }

    /**
     * Tests user logout functionality.
     */
    @Test
    public void testLogoutUser() {
        User testUser = createTestUser(1);
        userManager.loginUser(testUser);

        userManager.logoutUser();

        assertFalse(userManager.isLoggedIn());
        assertEquals(null, userManager.getCurrentUser());
        assertEquals(-1, userManager.getCurrentUserId());
    }

    /**
     * Helper method to create test user
     */
    private User createTestUser(int uid) {
        User user = new User();
        user.setUid(uid);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setProfilImage("default.png");
        return user;
    }

    /**
     * Cleans up after each test.
     * Logs out user and resets singleton instance.
     */
    @After
    public void tearDown() {
        if (userManager != null) {
            userManager.logoutUser();
        }
        UserManager.setInstance(null);
    }
}