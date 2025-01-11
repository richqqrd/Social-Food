package com.example.socialfood.camera;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialfood.R;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.gui.activities.MainActivity;
import com.example.socialfood.utils.ExampleData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for CameraFragment, tests camera functionality and UI elements.
 * Uses ActivityScenarioRule for MainActivity and requires camera permissions.
 */
@RunWith(AndroidJUnit4.class)
public class CameraFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(
            new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class)
                    .putExtra(MainActivity.EXTRA_SKIP_LOGIN, true));

    @Rule
    public GrantPermissionRule cameraPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.CAMERA);

    @Rule
    public GrantPermissionRule storagePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule locationPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    /**
     * Sets up test environment before each test.
     * Creates test user and navigates to camera.
     */
    @Before
    public void setup() {
        createTestUser();
        navigateToCamera();
    }

    /**
     * Creates a test user and populates the database with example data.
     */
    private void createTestUser() {
        Context context = ApplicationProvider.getApplicationContext();
        UserController userController = new UserController(context, null, null);
        PostController postController = new PostController(context, null, null, null, userController);
        ExampleData.populateDatabase(postController, userController);
    }

    /**
     * Navigates to the camera screen.
     */
    private void navigateToCamera() {
        onView(withId(R.id.nav_camera))
                .perform(click());
    }

    /**
     * Tests if camera fragment is displayed correctly.
     */
    @Test
    public void testCameraFragmentIsDisplayed() {
        onView(withId(R.id.fragment_camera_container))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if camera preview is displayed.
     */
    @Test
    public void testCameraPreviewIsDisplayed() {
        onView(withId(R.id.camera_preview))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if capture button is displayed and clickable.
     */
    @Test
    public void testCaptureButtonIsDisplayed() {
        onView(withId(R.id.capture_button))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests photo capture functionality.
     */
    @Test
    public void testPhotoCapture() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.capture_button))
                .perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.post_button))
                .perform(scrollTo())
                .check(matches(isDisplayed()));
    }
}