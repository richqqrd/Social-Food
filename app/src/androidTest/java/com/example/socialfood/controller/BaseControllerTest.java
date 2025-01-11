package com.example.socialfood.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.socialfood.model.entities.User;
import com.example.socialfood.utils.UserManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for BaseController. Tests basic controller functionality and user management.
 */
@RunWith(AndroidJUnit4.class)
public class BaseControllerTest {

    private TestBaseController baseController;
    private Context context;

    @Mock
    private UserManager mockUserManager;

    /**
     * Test implementation of BaseController for testing abstract class
     */
    private class TestBaseController extends com.example.socialfood.controller.BaseController {
        public TestBaseController(Context context) {
            super(context);
        }
    }

    /**
     * Sets up test environment before each test.
     * Creates mock UserManager and initializes controller.
     */
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        context = ApplicationProvider.getApplicationContext();
        baseController = new TestBaseController(context);
        baseController.setUserManager(mockUserManager);
    }

    /**
     * Tests if controller is initialized with context
     */
    @Test
    public void testInitialization() {
        TestBaseController controller = new TestBaseController(context);
        assertNotNull(controller);
    }

    /**
     * Tests getting current user
     */
    @Test
    public void testGetCurrentUser() {
        User mockUser = new User();
        mockUser.setUid(1);
        when(mockUserManager.getCurrentUser()).thenReturn(mockUser);

        User result = baseController.getCurrentUser();
        assertEquals(mockUser, result);
        verify(mockUserManager).getCurrentUser();
    }

    /**
     * Tests getting current user ID
     */
    @Test
    public void testGetCurrentUserId() {
        when(mockUserManager.getCurrentUserId()).thenReturn(1);

        int result = baseController.getCurrentUserId();
        assertEquals(1, result);
        verify(mockUserManager).getCurrentUserId();
    }

    /**
     * Tests setting custom UserManager
     */
    @Test
    public void testSetUserManager() {
        UserManager newMockManager = mock(UserManager.class);
        baseController.setUserManager(newMockManager);

        User mockUser = new User();
        when(newMockManager.getCurrentUser()).thenReturn(mockUser);

        User result = baseController.getCurrentUser();
        assertEquals(mockUser, result);
        verify(newMockManager).getCurrentUser();
    }
}